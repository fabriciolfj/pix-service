package com.payment.models;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class WebhookRegistration {

    private UUID id;
    private String merchantId;
    private String callbackUrl;
    private String secret;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public static WebhookRegistration create(String merchantId, String callbackUrl, String secret) {
        return WebhookRegistration.builder()
                .id(UUID.randomUUID()).merchantId(merchantId).callbackUrl(callbackUrl)
                .secret(secret).active(true).createdAt(Instant.now()).updatedAt(Instant.now())
                .build();
    }

    public WebhookRegistration deactivate() {
        return toBuilder().active(false).updatedAt(Instant.now()).build();
    }
}
