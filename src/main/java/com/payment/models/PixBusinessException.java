package com.payment.models;

import lombok.Getter;

/**
 * Exceção de domínio para violações das regras do Pix.
 * Carrega um código padronizado para mapeamento no handler HTTP.
 */
@Getter
public class PixBusinessException extends RuntimeException {

    private final String code;

    public PixBusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
}
