package com.payment.exceptions;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private Response.Status statusCode;

    public ApplicationException(final String msg, final Response.Status statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }
}
