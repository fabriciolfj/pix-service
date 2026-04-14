package com.payment.models;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class OutboxEvent {

    private UUID id;
    private String aggregateId;
    private String aggregateType;
    private String eventType;
    private String payload;
    private String topic;
    private OutboxStatus status;
    private Instant createdAt;
    private Instant processedAt;
    private int retryCount;

    public static OutboxEvent create(String aggregateId, String aggregateType,
                                     String eventType, String payload, String topic) {
        return OutboxEvent.builder()
                .id(UUID.randomUUID()).aggregateId(aggregateId).aggregateType(aggregateType)
                .eventType(eventType).payload(payload).topic(topic)
                .status(OutboxStatus.PENDING).createdAt(Instant.now()).retryCount(0).build();
    }

    public OutboxEvent markPublished() {
        return toBuilder().status(OutboxStatus.PUBLISHED).processedAt(Instant.now()).build();
    }

    public OutboxEvent markFailed() {
        return toBuilder().status(OutboxStatus.FAILED).retryCount(retryCount + 1).build();
    }

    public enum OutboxStatus { PENDING, PUBLISHED, FAILED }
}
