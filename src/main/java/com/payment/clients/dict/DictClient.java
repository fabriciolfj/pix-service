package com.payment.clients.dict;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "pix-dict")
public interface DictClient {

    @GET
    @Path("/api/v2/dict/key/{key}")
    Uni<DictKeyResponse> getKey(@PathParam("key") final String key);
}
