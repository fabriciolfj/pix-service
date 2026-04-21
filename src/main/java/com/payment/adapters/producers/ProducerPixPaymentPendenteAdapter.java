package com.payment.adapters.producers;

import com.payment.adapters.getpayment.FindPixPaymentAdapter;
import com.payment.models.OutboxEvent;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class ProducerPixPaymentPendenteAdapter {

    @Inject
    @Channel("processing-payment-out")
    Emitter<String> emitter;
    private final FindPixPaymentAdapter findPixPaymentAdapter;

    @WithSession
    public Uni<Void> process() {
        return findPixPaymentAdapter
                .getByStatus(OutboxEvent.OutboxStatus.PENDING.name())
                .invoke(item -> log.info("outbox found {}", item.getId()))
                .onItem()
                .transform(v -> emitter.send(v.getPayload()))
                .toUni().replaceWithVoid();
    }
}
