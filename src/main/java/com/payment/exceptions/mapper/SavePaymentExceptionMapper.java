package com.payment.exceptions.mapper;

import com.payment.exceptions.SavePaymentException;
import com.payment.exceptions.model.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SavePaymentExceptionMapper implements ExceptionMapper<SavePaymentException> {

    @Override
    public Response toResponse(SavePaymentException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(exception.getMessage()))
                .build();
    }
}
