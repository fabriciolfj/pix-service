package com.payment.exceptions;

public class DictClientBadRequestException extends RuntimeException {

    public DictClientBadRequestException(final String msg) {
        super(msg);
    }
}
