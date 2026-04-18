package com.payment.exceptions;

import jakarta.ws.rs.core.Response;

public class SavePaymentException extends ApplicationException {

    public SavePaymentException(final String msg) {
        super(msg, Response.Status.INTERNAL_SERVER_ERROR);
    }
}
