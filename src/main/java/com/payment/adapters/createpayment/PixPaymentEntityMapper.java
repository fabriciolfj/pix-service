package com.payment.adapters.createpayment;

import com.payment.models.PixPayment;
import com.payment.persistences.entities.PixPaymentEntity;

public class PixPaymentEntityMapper {

    private PixPaymentEntityMapper() { }

    public static PixPaymentEntity toEntity(final PixPayment domain) {
        return PixPaymentEntity.builder()
                .id(domain.getId())
                .endToEndId(domain.getEndToEndId())
                .payerKey(domain.getPayerKey().getKey())
                .payerKeyType(domain.getPayerKey().getKeyType().name())
                .receiverKey(domain.getReceiverKey().getKey())
                .receiverKeyType(domain.getReceiverKey().getKeyType().name())
                .receiverIspb(domain.getReceiverKey().getBankIspb())
                .receiverName(domain.getReceiverKey().getOwnerName())
                .receiverDocument(domain.getReceiverKey().getOwnerDocument())
                .amount(domain.getAmount())
                .description(domain.getDescription())
                .status(domain.getStatus().toString())
                .type(domain.getType().toString())
                .txId(domain.getTxId())
                .retryCount(domain.getRetryCount())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
