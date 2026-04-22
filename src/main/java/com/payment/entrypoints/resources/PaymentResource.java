package com.payment.entrypoints.resources;

import com.payment.entrypoints.dtos.request.CreatePixPaymentRequest;
import com.payment.entrypoints.mappers.PixPaymentResourceMapper;
import com.payment.exceptions.ApplicationException;
import com.payment.exceptions.mapper.ErrorResponseMapper;
import com.payment.usecases.create.CreatePaymentUseCase;
import com.payment.usecases.processingpix.ProducerPixPaymentPendenteGateway;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Path("/api/v1/pix/pagamentos")
public class PaymentResource {

    private final CreatePaymentUseCase useCase;
    private final PixPaymentResourceMapper mapper;
    private final ProducerPixPaymentPendenteGateway producerPixPaymentPendenteGateway;

    public PaymentResource(final CreatePaymentUseCase useCase,
                           final PixPaymentResourceMapper mapper,
                           final ProducerPixPaymentPendenteGateway producerPixPaymentPendenteGateway) {
        this.useCase = useCase;
        this.mapper = mapper;
        this.producerPixPaymentPendenteGateway = producerPixPaymentPendenteGateway;
    }

    @POST
    public Uni<Response> createPayment(@Valid final CreatePixPaymentRequest request) {
        return Uni.createFrom().item(request)
                .map(mapper::toEntity)
                .call(useCase::execute)
                .invoke(pix -> log.info("pix created {}", pix.getId()))
                .map(mapper::toResponse)
                .map(response -> Response.status(Response.Status.ACCEPTED)
                        .entity(response)
                        .build());
    }

    @POST
    @Path("/processing")
    public Uni<Response> initProcessPayment() {
        return producerPixPaymentPendenteGateway.process()
                .map(_ -> Response.accepted().build());
    }
}
