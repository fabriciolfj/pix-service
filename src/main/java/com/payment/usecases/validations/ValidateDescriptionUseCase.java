package com.payment.usecases.validations;

import com.payment.models.PixBusinessException;
import com.payment.models.PixPayment;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ValidateDescriptionUseCase implements PixValidationRulesUseCase {
    private static final int MAX_DESCRIPTION_LENGTH      = 140;

    @Override
    public Uni<PixPayment> execute(PixPayment payment) {
        return Uni.createFrom().item(payment)
                .invoke(p -> log.info("validation description started to {}", payment.getId()))
                .invoke(this::validateDescription);
    }

    private void validateDescription(PixPayment p) {
        if (p.getDescription() != null && p.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new PixBusinessException("PIX_004",
                    "Descrição excede 140 caracteres.");
        }
    }
}
