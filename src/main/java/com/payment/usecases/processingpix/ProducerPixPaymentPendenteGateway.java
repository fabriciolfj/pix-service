package com.payment.usecases.processingpix;

import io.smallrye.mutiny.Uni;

public interface ProducerPixPaymentPendenteGateway {

    Uni<Void> process();
}
