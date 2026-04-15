package com.payment.adapters;

import com.payment.clients.dict.DicKeyResponseMapper;
import com.payment.clients.dict.DictClient;
import com.payment.exceptions.DictClientBadRequestException;
import com.payment.exceptions.DictClientServerException;
import com.payment.models.PixKey;
import com.payment.usecases.create.DictGateway;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@ApplicationScoped
public class DictAdapter implements DictGateway {

    private DictClient dictClient;

    public DictAdapter(@RestClient final DictClient dictClient) {
        this.dictClient = dictClient;
    }

    @Override
    public Uni<PixKey> process(final String key) {
        return Uni.createFrom().item(key)
                .invoke(k -> log.info("receibe key {}, init request dict", k))
                .flatMap(k -> dictClient.getKey(k))
                .onFailure(WebApplicationException.class)
                .transform(ex -> {
                    int status = ex.getResponse().getStatus();
                    if (status >= 400 && status < 500) {
                        return new DictClientBadRequestException("Dict returned 4xx for key: " + key);
                    }
                    if (status >= 500) {
                        return new DictClientServerException("Dict returned 5xx for key: " + key);
                    }
                    return ex;
                })
                .map(DicKeyResponseMapper::toPixKey);
    }
}
