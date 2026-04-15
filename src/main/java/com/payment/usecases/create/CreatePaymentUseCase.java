package com.payment.usecases.create;

import com.payment.models.PixPayment;
import com.payment.usecases.validations.PixValidationUseCase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreatePaymentUseCase {

    private DictGateway dictGateway;
    private PixValidationUseCase pixValidationUseCase;

    public CreatePaymentUseCase(final DictGateway dictGateway, final PixValidationUseCase pixValidationUseCase) {
        this.dictGateway = dictGateway;
        this.pixValidationUseCase = pixValidationUseCase;
    }

    public Uni<PixPayment> execute(final PixPayment payment) {
        return Uni.createFrom().item(payment)
                .flatMap(p -> dictGateway.process(p.getReceiveKey()))
                .map(pixKey -> payment.toBuilder()
                        .payerKey(pixKey)
                        .build())
                .flatMap(pixValidationUseCase::execute);
    }
}
