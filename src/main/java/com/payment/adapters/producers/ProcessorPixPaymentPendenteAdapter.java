package com.payment.adapters.producers;

import com.payment.adapters.outbox.FindOutboxByStatusAdapter;
import com.payment.models.OutboxEvent;
import com.payment.persistences.entities.OutboxEventEntity;
import com.payment.usecases.processingpix.ProducerPixPaymentPendenteGateway;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
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
                .transformToUniAndConcatenate(this::sendEvent)
                .collect().asList()
                .replaceWithVoid();
    }

    private Uni<Void> sendEvent(OutboxEventEntity outboxEvent) {
        return Uni.createFrom()
                .item(outboxEvent)
                .invoke(v -> emitter.send(v.getPayload()))
                .flatMap(v -> {
                    v.setStatus(OutboxEvent.OutboxStatus.PUBLISHED.name());
                    return v.<OutboxEventEntity>persistAndFlush()
                            .replaceWithVoid();
                })
                .onFailure().recoverWithUni(ex -> {
                    log.error("falha ao processar outbox id={}: {}",
                            outboxEvent.getId(), ex.getMessage());
                    outboxEvent.setStatus(OutboxEvent.OutboxStatus.FAILED.name());
                    outboxEvent.setRetryCount(outboxEvent.getRetryCount() + 1);
                    return outboxEvent.<OutboxEventEntity>persistAndFlush()
                            .replaceWithVoid();
                });
    }
}
