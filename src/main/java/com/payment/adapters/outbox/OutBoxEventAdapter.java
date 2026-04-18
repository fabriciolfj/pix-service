package com.payment.adapters.outbox;

import com.payment.models.OutboxEvent;
import com.payment.persistences.entities.OutboxEventEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class OutBoxEventAdapter {

    public Uni<Void> process(final OutboxEvent outboxEvent) {
        return Uni.createFrom().item(outboxEvent)
                .map(OutBoxEventEntityMapper::toEntity)
                .flatMap(PanacheEntityBase::persistAndFlush)
                .map(entity -> (OutboxEventEntity) entity)
                .invoke(entity -> log.info("outbox event save success {}", entity.getAggregateId()))
                .replaceWithVoid();
    }
}
