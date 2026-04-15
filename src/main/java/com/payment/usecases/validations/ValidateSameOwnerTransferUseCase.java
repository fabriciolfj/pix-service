package com.payment.usecases.validations;

import com.payment.models.PixPayment;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ValidateSameOwnerTransferUseCase implements PixValidationRulesUseCase {

    @Override
    public Uni<PixPayment> execute(PixPayment payment) {
        return Uni.createFrom()
                .item(payment)
                .invoke(p -> log.info("validation same owner transfer started to {}", payment.getId()))
                .map(PixPayment::validateSameOwnerTransfer);
    }
}
