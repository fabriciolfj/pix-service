package com.payment.clients.spi;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "spi")
public interface SpiClient {

    @POST
    @Path("/api/v1/in/{ispb}/stream")
    Uni<Void> requestPayment(@PathParam("ispb") final String isbp, final String body);
}
