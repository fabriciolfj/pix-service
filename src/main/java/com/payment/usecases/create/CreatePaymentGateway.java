package com.payment.usecases.create;

import com.payment.models.PixPayment;
import io.smallrye.mutiny.Uni;

public interface CreatePaymentGateway {

    Uni<PixPayment> process(final PixPayment payment);
}
