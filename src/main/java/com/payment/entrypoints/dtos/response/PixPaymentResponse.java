package com.payment.entrypoints.dtos.response;

import com.payment.models.PixPaymentType;
import com.payment.models.PixStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PixPaymentResponse(
        UUID id,
        String endToEndId,
        String payerKey,
        String receiverKey,
        BigDecimal amount,
        String description,
        PixStatus status,
        PixPaymentType type,
        Instant createdAt,
        Instant updatedAt
) {}
