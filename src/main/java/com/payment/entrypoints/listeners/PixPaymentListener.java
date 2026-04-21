package com.payment.entrypoints.listeners;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Slf4j
@ApplicationScoped
public class PixPaymentListener {

    @Incoming("processing-payment-in")
    public Uni<Void> receive(final String message) throws InterruptedException {
        return Uni.createFrom()
                .item(message)
                .invoke(msg -> log.info("message receive success {}", msg))
                .replaceWithVoid();
    }
}
