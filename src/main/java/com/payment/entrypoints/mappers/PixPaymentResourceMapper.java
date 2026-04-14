package com.payment.entrypoints.mappers;

import com.payment.entrypoints.dtos.request.CreatePixPaymentRequest;
import com.payment.entrypoints.dtos.response.PixPaymentResponse;
import com.payment.models.PixKey;
import com.payment.models.PixPayment;
import com.payment.models.PixStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.Instant;
import java.util.UUID;

import static com.payment.models.PixPayment.generateEndToEndId;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface PixPaymentResourceMapper {

    default PixPayment toEntity(final CreatePixPaymentRequest request) {
        return PixPayment.builder()
                .id(UUID.randomUUID())
                .endToEndId(generateEndToEndId())
                .payerKey(PixKey.of(request.payerKey().key(), request.payerKey().keyType()))
                .receiverKey(PixKey.of(request.receiverKey().key(), request.receiverKey().keyType()))
                .amount(request.amount())
                .description(request.description())
                .status(PixStatus.PENDING)
                .type(request.type())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .retryCount(0)
                .txId(request.txId())
                .build();
    }

    @Mapping(target = "payerKey", expression = "java(payment.getPayerKey().getKey())")
    @Mapping(target = "receiverKey", expression = "java(payment.getReceiverKey().getKey())")
    PixPaymentResponse toResponse(PixPayment payment);
}
