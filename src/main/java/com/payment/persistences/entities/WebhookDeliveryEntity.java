package com.payment.persistences.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "webhook_delivery")
public class WebhookDeliveryEntity extends PanacheEntityBase {

    @Id
    private UUID id;

    @Column(name = "webhook_registration_id")
    private UUID webhookRegistrationId;

    @Column(name = "end_to_end_id")
    private String endToEndId;

    @Column(name = "event_type")
    private String eventType;

    private String payload;
    private String status;

    @Column(name = "attempt_count")
    private int attemptCount;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;

    @Column(name = "last_attempt_at")
    private Instant lastAttemptAt;

    @Column(name = "last_http_status")
    private Integer lastHttpStatus;

    @Column(name = "last_error_message")
    private String lastErrorMessage;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;
}
