package com.payment.exceptions.mapper;

import com.payment.exceptions.model.ErrorResponse;

public class ErrorResponseMapper {

    private ErrorResponseMapper() { }

    public static ErrorResponse toError(final String message) {
        return new ErrorResponse(message);
    }
}
