package com.payment.usecases.validations;

import com.payment.models.PixPayment;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class PixValidationUseCase {

    public Instance<PixValidationRulesUseCase> rulesUseCases;

    public PixValidationUseCase(final Instance<PixValidationRulesUseCase> rulesUseCases) {
        this.rulesUseCases = rulesUseCases;
    }

    public Uni<PixPayment> execute(final PixPayment payment) {
        return Multi.createFrom()
                .iterable(rulesUseCases)
                .onItem()
                .transformToUniAndMerge(impl -> impl.execute(payment))
                .collect().last();

    }
}
