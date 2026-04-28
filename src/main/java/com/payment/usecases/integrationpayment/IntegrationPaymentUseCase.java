package com.payment.usecases.integrationpayment;

import com.payment.models.PixPayment;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class IntegrationPaymentUseCase {

    private final SpiGateway spiGateway;

    public Uni<Void> execute(final PixPayment payment) {
        return spiGateway.process(payment);
    }
}
