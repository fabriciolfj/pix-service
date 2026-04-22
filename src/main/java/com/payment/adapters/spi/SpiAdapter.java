package com.payment.adapters.spi;

import com.payment.clients.spi.SpiClient;
import com.payment.clients.spi.dto.Pacs008Document;
import com.payment.models.PixPayment;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;

import java.io.StringWriter;

//@ApplicationScoped
public class SpiAdapter {

    private final JAXBContext pacs008JaxbContext;
    private final SpiClient spiClient;
    @ConfigProperty(name = "pix.spi.ispb")
    String ispb;

    public SpiAdapter(JAXBContext pacs008JaxbContext, SpiClient spiClient) {
        this.pacs008JaxbContext = pacs008JaxbContext;
        this.spiClient = spiClient;
    }

    @Retry(maxRetries = 4)
    public Uni<Void> process(final PixPayment payment) {
        return spiClient.requestPayment(ispb, "");
    }

    private String marshal(Pacs008Document document) throws Exception {
        Marshaller m = pacs008JaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        StringWriter sw = new StringWriter();
        m.marshal(document, sw);
        return sw.toString();
    }
}
