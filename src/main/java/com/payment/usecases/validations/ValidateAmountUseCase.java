package com.payment.usecases.validations;

import com.payment.models.PixBusinessException;
import com.payment.models.PixPayment;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@ApplicationScoped
public class ValidateAmountUseCase implements PixValidationRulesUseCase {

    private static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");

    @Override
    public Uni<PixPayment> execute(final PixPayment payment) {
        return Uni.createFrom().item(payment)
                .invoke(p -> log.info("validation amount started to {}", payment.getId()))
                .invoke(this::validateAmount);
    }

    private void validateAmount(PixPayment p) {
        final var amount = p.getAmount();
        if (amount == null || amount.compareTo(MIN_AMOUNT) < 0) {
            throw new PixBusinessException("PIX_001",
                    "Valor inválido. Mínimo permitido: R$ 0,01");
        }
    }
}
