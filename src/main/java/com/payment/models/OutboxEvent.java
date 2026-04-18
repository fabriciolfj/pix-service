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
    private String eventType;
    private String payload;
    private String topic;
    private OutboxStatus status;
    private Instant createdAt;
    private Instant processedAt;
    private int retryCount;

    public OutboxEvent markPublished() {
        return toBuilder().status(OutboxStatus.PUBLISHED).processedAt(Instant.now()).build();
    }

    public OutboxEvent markFailed() {
        return toBuilder().status(OutboxStatus.FAILED).retryCount(retryCount + 1).build();
    }

    public enum OutboxStatus { PENDING, PUBLISHED, FAILED }
}
