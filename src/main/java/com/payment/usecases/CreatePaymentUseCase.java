package com.payment.usecases;

import com.payment.models.PixPayment;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreatePaymentUseCase {

    public Uni<PixPayment> execute(final PixPayment payment) {
        return Uni.createFrom().item(payment);
    }
}
