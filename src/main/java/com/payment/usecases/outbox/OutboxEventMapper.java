package com.payment.usecases.outbox;

import com.payment.models.OutboxEvent;
import com.payment.models.OutboxEvent.OutboxStatus;

import java.time.Instant;
import java.util.UUID;

public class OutboxEventMapper {

    private OutboxEventMapper() { }

    public static OutboxEvent toOutBox(final String id,
                                       final String type,
                                       final String topic,
                                       final String payload) {
        return OutboxEvent.builder()
                .id(UUID.randomUUID()).aggregateId(id)
                .eventType(type).payload(payload).topic(topic)
                .status(OutboxStatus.PENDING).createdAt(Instant.now()).retryCount(0).build();
    }

}
