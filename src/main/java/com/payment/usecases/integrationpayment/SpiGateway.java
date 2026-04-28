package com.payment.usecases.integrationpayment;

import com.payment.models.PixPayment;
import io.smallrye.mutiny.Uni;

public interface SpiGateway {

    Uni<Void> process(final PixPayment payment);
}
