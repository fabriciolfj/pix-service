package com.payment.exceptions.mapper;


import com.payment.exceptions.model.MessageErrorDetails;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Provider
public class ConstraintExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private ConstraintViolation<?> cv;

    @Override
    public Response toResponse(ConstraintViolationException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(prepareMessage(e))
                .build();
    }

    private List<MessageErrorDetails> prepareMessage(ConstraintViolationException exception) {
        final List<MessageErrorDetails> messages = new ArrayList<>();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            messages.add(new MessageErrorDetails(cv.getPropertyPath().toString(), cv.getMessage()));
        }
        return messages;
    }
}
