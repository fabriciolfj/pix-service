package com.payment.adapters.spi;

import com.payment.clients.spi.SpiClient;
import com.payment.clients.spi.SpiMapper;
import com.payment.clients.spi.dto.Pacs008Document;
import com.payment.models.PixPayment;
import com.payment.usecases.integrationpayment.SpiGateway;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.StringWriter;

@Slf4j
@ApplicationScoped
public class SpiAdapter implements SpiGateway {

    private final JAXBContext pacs008JaxbContext;
    private final SpiClient spiClient;
    @ConfigProperty(name = "pix.spi.ispb")
    String ispb;

    public SpiAdapter(JAXBContext pacs008JaxbContext, @RestClient SpiClient spiClient) {
        this.pacs008JaxbContext = pacs008JaxbContext;
        this.spiClient = spiClient;
    }

    @Override
    @Retry(maxRetries = 4)
    @CircuitBreaker(requestVolumeThreshold = 20, delay = 5000L)
    public Uni<Void> process(final PixPayment payment) {
        return Uni.createFrom().item(payment)
                .onItem().transform(p -> marshal(SpiMapper.toJaxbDocument(p, ispb)))
                .invoke(v -> log.info("payment transform to xml success {}", v))
                .onItem().transformToUni(value -> spiClient.requestPayment(ispb, value))
                .invoke(v -> log.info("payment accept success {}", payment.getEndToEndId()));
    }

    private String marshal(Pacs008Document document) {
        try {
            Marshaller m = null;
            m = pacs008JaxbContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            StringWriter sw = new StringWriter();
            m.marshal(document, sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
