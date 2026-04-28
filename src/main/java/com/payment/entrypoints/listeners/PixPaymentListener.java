package com.payment.entrypoints.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.models.PixPayment;
import com.payment.usecases.integrationpayment.IntegrationPaymentUseCase;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Slf4j
@ApplicationScoped
public class PixPaymentListener {

    private final ObjectMapper objectMapper;
    private final IntegrationPaymentUseCase integrationPaymentUseCase;

    public PixPaymentListener(ObjectMapper objectMapper, IntegrationPaymentUseCase integrationPaymentUseCase) {
        this.objectMapper = objectMapper;
        this.integrationPaymentUseCase = integrationPaymentUseCase;
    }

    //pendente dlq kafka
    @Incoming("processing-payment-in")
    public Uni<Void> receive(final String message) {
        return Uni.createFrom()
                .item(message)
                .onItem()
                .transform(Unchecked.function(value -> {
                    try {
                        return objectMapper.readValue(value, PixPayment.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .invoke(payment -> log.info("payment receive success {}", payment.getId()))
                .onItem().transformToUni(integrationPaymentUseCase::execute)
                .replaceWithVoid()
                .onFailure()
                .invoke(err -> log.error("fail deserialize payload, details {}", err.getMessage()));
    }
}
