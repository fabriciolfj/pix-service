package com.payment.persistences.entities;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pix_payment")
public class PixPaymentEntity extends PanacheEntityBase {

    @Id
    private UUID code;

    @Column(name = "end_to_end_id")
    private String endToEndId;

    @Column(name = "payer_key")
    private String payerKey;

    @Column(name = "payer_key_type")
    private String payerKeyType;

    @Column(name = "receiver_key")
    private String receiverKey;

    @Column(name = "receiver_key_type")
    private String receiverKeyType;

    @Column(name = "receiver_ispb")
    private String receiverIspb;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_document")
    private String receiverDocument;

    private BigDecimal amount;
    private String description;
    private String status;
    private String type;

    @Column(name = "tx_id")
    private String txId;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
