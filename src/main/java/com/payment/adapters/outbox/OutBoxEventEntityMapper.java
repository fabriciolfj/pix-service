package com.payment.adapters.outbox;

import com.payment.models.OutboxEvent;
import com.payment.persistences.entities.OutboxEventEntity;

public class OutBoxEventEntityMapper {

    private OutBoxEventEntityMapper() { }

    public static OutboxEventEntity toEntity(final OutboxEvent domain) {
        return OutboxEventEntity.builder()
                .id(domain.getId())
                .aggregateId(domain.getAggregateId())
                .eventType(domain.getEventType())
                .payload(domain.getPayload())
                .topic(domain.getTopic())
                .status(domain.getStatus().toString())
                .retryCount(domain.getRetryCount())
                .createdAt(domain.getCreatedAt())
                .processedAt(domain.getProcessedAt())
                .build();
    }
}
