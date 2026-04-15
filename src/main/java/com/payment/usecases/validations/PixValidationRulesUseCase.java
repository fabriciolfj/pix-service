package com.payment.usecases.validations;

import com.payment.models.PixPayment;
import io.smallrye.mutiny.Uni;

public interface PixValidationRulesUseCase {

    Uni<PixPayment> execute(final PixPayment payment);
}
