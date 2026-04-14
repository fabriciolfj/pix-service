package com.payment.entrypoints.dtos.request;

import com.payment.models.PixKey;
import com.payment.models.PixPaymentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreatePixPaymentRequest(

        @Valid @NotNull
        PixKeyDto payerKey,

        @Valid @NotNull
        PixKeyDto receiverKey,

        @NotNull
        @DecimalMin(value = "0.01", message = "Valor mínimo de R$ 0,01")
        @DecimalMax(value = "999999999.99", message = "Valor máximo de R$ 999.999.999,99")
        BigDecimal amount,

        @Size(max = 140, message = "Descrição deve ter no máximo 140 caracteres")
        String description,

        @NotNull
        PixPaymentType type,

        @Size(min = 26, max = 35)
        String txId

) {

    public record PixKeyDto(
            @NotBlank String key,
            @NotNull PixKey.PixKeyType keyType
    ) {}
}
