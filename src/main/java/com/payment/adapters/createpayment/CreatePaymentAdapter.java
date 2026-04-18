package com.payment.adapters.createpayment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.payment.adapters.outbox.OutBoxEventAdapter;
import com.payment.exceptions.SavePaymentException;
import com.payment.models.OutboxEvent;
import com.payment.models.PixPayment;
import com.payment.persistences.entities.PixPaymentEntity;
import com.payment.usecases.create.CreatePaymentGateway;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@ApplicationScoped
public class CreatePaymentAdapter implements CreatePaymentGateway {

    private static final String TYPE_AGGREGATE = "PixPayment";

    @ConfigProperty(name = "pix.pagamento.solicitado")
    String topic;

    private OutBoxEventAdapter outBoxEventAdapter;
    private ObjectMapper jsonMapper;

    public CreatePaymentAdapter(final OutBoxEventAdapter outBoxEventAdapter, final ObjectMapper jsonMapper) {
        this.outBoxEventAdapter = outBoxEventAdapter;
        this.jsonMapper = jsonMapper;
    }

    @Override
    @WithTransaction
    public Uni<PixPayment> process(PixPayment payment) {
        return Uni.createFrom().item(payment)
                .onItem()
                .transform(PixPaymentEntityMapper::toEntity)
                .flatMap(entity -> entity.persistAndFlush())
                .map(obj -> (PixPaymentEntity) obj)
                .invoke(entity -> log.info("payment save success {}", entity.getCode()))
                .map(_ -> OutboxEvent.create(
                            payment.getId().toString(), TYPE_AGGREGATE, topic,
                            getJsonPayload(payment),
                            topic

                    ))
                .flatMap(outBoxEventAdapter::process)
                .replaceWith(payment)
                .onFailure()
                .transform(e -> new SavePaymentException("fail save payment, details " + e.getMessage()));
    }

    private String getJsonPayload(PixPayment payment) {
        try {
            return jsonMapper.writeValueAsString(payment);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
