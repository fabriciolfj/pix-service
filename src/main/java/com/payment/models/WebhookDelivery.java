package com.payment.models;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class WebhookDelivery {

    private UUID id;
    private UUID webhookRegistrationId;
    private String endToEndId;
    private String eventType;
    private String payload;
    private WebhookDeliveryStatus status;
    private int attemptCount;
    private Instant nextRetryAt;
    private Instant lastAttemptAt;
    private Integer lastHttpStatus;
    private String lastErrorMessage;
    private Instant createdAt;

    private static final int MAX_ATTEMPTS = 5;
    private static final long[] BACKOFF_SECONDS = {30, 120, 600, 3600, 21600};

    public static WebhookDelivery create(UUID webhookRegistrationId, String endToEndId,
                                         String eventType, String payload) {
        return WebhookDelivery.builder()
                .id(UUID.randomUUID()).webhookRegistrationId(webhookRegistrationId)
                .endToEndId(endToEndId).eventType(eventType).payload(payload)
                .status(WebhookDeliveryStatus.PENDING).attemptCount(0)
                .nextRetryAt(Instant.now()).createdAt(Instant.now()).build();
    }

    public WebhookDelivery markSuccess(int httpStatus) {
        return toBuilder().status(WebhookDeliveryStatus.DELIVERED)
                .lastHttpStatus(httpStatus).lastAttemptAt(Instant.now())
                .attemptCount(attemptCount + 1).build();
    }

    public WebhookDelivery markFailure(String errorMessage, Integer httpStatus) {
        int next = attemptCount + 1;
        boolean exhausted = next >= MAX_ATTEMPTS;
        Instant nextRetry = exhausted ? null
                : Instant.now().plusSeconds(BACKOFF_SECONDS[Math.min(attemptCount, BACKOFF_SECONDS.length - 1)]);

        return toBuilder()
                .status(exhausted ? WebhookDeliveryStatus.FAILED : WebhookDeliveryStatus.RETRYING)
                .lastHttpStatus(httpStatus).lastErrorMessage(errorMessage)
                .lastAttemptAt(Instant.now()).attemptCount(next).nextRetryAt(nextRetry).build();
    }

    public boolean canRetry() {
        return status == WebhookDeliveryStatus.RETRYING && attemptCount < MAX_ATTEMPTS;
    }

    public enum WebhookDeliveryStatus { PENDING, RETRYING, DELIVERED, FAILED }
}
