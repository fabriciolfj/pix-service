package com.payment.usecases.create;

import com.payment.models.PixKey;
import io.smallrye.mutiny.Uni;

public interface DictGateway {

    Uni<PixKey> process(final String key);
}
