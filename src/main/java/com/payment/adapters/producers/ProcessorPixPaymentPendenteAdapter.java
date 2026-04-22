package com.payment.adapters.producers;

import com.payment.adapters.outbox.FindOutboxByStatusAdapter;
import com.payment.models.OutboxEvent;
import com.payment.persistences.entities.OutboxEventEntity;
import com.payment.usecases.processingpix.ProducerPixPaymentPendenteGateway;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Multi;
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
public class ProcessorPixPaymentPendenteAdapter implements ProducerPixPaymentPendenteGateway {

    @Inject
    @Channel("processing-payment-out")
    Emitter<String> emitter;
    private final FindOutboxByStatusAdapter findPixPaymentAdapter;

    @WithTransaction
    public Uni<Void> process() {
        return findPixPaymentAdapter
                .getByStatus(OutboxEvent.OutboxStatus.PENDING.name())
                .invoke(item -> log.info("outbox found {}", item.getId()))
                .onItem()
                .invoke(v -> emitter.send(v.getPayload()))
                .onItem()
                .transformToUniAndConcatenate(outboxEvent -> {
                    outboxEvent.setStatus(OutboxEvent.OutboxStatus.PUBLISHED.name());
                    return outboxEvent.<OutboxEventEntity>persistAndFlush()
                            .replaceWithVoid();
                })
                .collect().asList()
                .replaceWithVoid();
    }
}
