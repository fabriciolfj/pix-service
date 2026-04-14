package com.payment.models;

import java.util.regex.Pattern;

/**
 * Validações das chaves Pix conforme especificação BACEN.
 *
 * Ref: Manual de Interfaces da API Pix — Seção 4 (DICT)
 */
public class PixKeyValidator {

    // Padrões conforme spec BACEN
    private static final Pattern CPF_PATTERN   = Pattern.compile("^\\d{11}$");
    private static final Pattern CNPJ_PATTERN  = Pattern.compile("^\\d{14}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+55\\d{10,11}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]{1,77}@[^@\\s\\.]{1,67}(\\.[^@\\s\\.]{1,67}){1,3}$");
    private static final Pattern EVP_PATTERN   = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

    /** Tamanho máximo de uma chave Pix — qualquer tipo */
    private static final int MAX_KEY_LENGTH = 77;

    public static void validate(String key, PixKey.PixKeyType keyType) {
        if (key == null || key.isBlank()) {
            throw new PixBusinessException("PIX_KEY_001", "Chave Pix não pode ser vazia.");
        }
        if (key.length() > MAX_KEY_LENGTH) {
            throw new PixBusinessException("PIX_KEY_002", "Chave Pix excede 77 caracteres.");
        }

        switch (keyType) {
            case CPF   -> validateCpf(key);
            case CNPJ  -> validateCnpj(key);
            case PHONE -> validatePhone(key);
            case EMAIL -> validateEmail(key);
            case EVP   -> validateEvp(key);
        }
    }

    // -----------------------------------------------------------------------
    // CPF — 11 dígitos sem pontuação + dígitos verificadores
    // -----------------------------------------------------------------------
    static void validateCpf(String cpf) {
        if (!CPF_PATTERN.matcher(cpf).matches()) {
            throw new PixBusinessException("PIX_KEY_003",
                "CPF inválido. Formato esperado: 11 dígitos numéricos sem pontuação.");
        }
        if (!isCpfValid(cpf)) {
            throw new PixBusinessException("PIX_KEY_004",
                "CPF com dígitos verificadores inválidos.");
        }
    }

    // -----------------------------------------------------------------------
    // CNPJ — 14 dígitos sem pontuação + dígitos verificadores
    // -----------------------------------------------------------------------
    static void validateCnpj(String cnpj) {
        if (!CNPJ_PATTERN.matcher(cnpj).matches()) {
            throw new PixBusinessException("PIX_KEY_005",
                "CNPJ inválido. Formato esperado: 14 dígitos numéricos sem pontuação.");
        }
        if (!isCnpjValid(cnpj)) {
            throw new PixBusinessException("PIX_KEY_006",
                "CNPJ com dígitos verificadores inválidos.");
        }
    }

    // -----------------------------------------------------------------------
    // Telefone — +55 seguido de DDD (2 dígitos) + número (8 ou 9 dígitos)
    // -----------------------------------------------------------------------
    static void validatePhone(String phone) {
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new PixBusinessException("PIX_KEY_007",
                "Telefone inválido. Formato esperado: +55DDD[8ou9dígitos]. Ex: +5511987654321");
        }
    }

    // -----------------------------------------------------------------------
    // E-mail — máximo 77 chars (conforme DICT spec)
    // -----------------------------------------------------------------------
    static void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new PixBusinessException("PIX_KEY_008",
                "E-mail inválido. Máximo 77 caracteres no formato usuario@dominio.com");
        }
    }

    // -----------------------------------------------------------------------
    // EVP — UUID v4 gerado aleatoriamente pelo DICT
    // -----------------------------------------------------------------------
    static void validateEvp(String evp) {
        if (!EVP_PATTERN.matcher(evp.toLowerCase()).matches()) {
            throw new PixBusinessException("PIX_KEY_009",
                "EVP inválido. Deve ser um UUID v4 no formato xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
        }
    }

    // -----------------------------------------------------------------------
    // Algoritmo de validação CPF
    // -----------------------------------------------------------------------
    static boolean isCpfValid(String cpf) {
        if (cpf.chars().distinct().count() == 1) return false; // ex: "11111111111"

        int sum = 0;
        for (int i = 0; i < 9; i++) sum += (cpf.charAt(i) - '0') * (10 - i);
        int d1 = 11 - (sum % 11);
        if (d1 >= 10) d1 = 0;
        if (d1 != (cpf.charAt(9) - '0')) return false;

        sum = 0;
        for (int i = 0; i < 10; i++) sum += (cpf.charAt(i) - '0') * (11 - i);
        int d2 = 11 - (sum % 11);
        if (d2 >= 10) d2 = 0;
        return d2 == (cpf.charAt(10) - '0');
    }

    // -----------------------------------------------------------------------
    // Algoritmo de validação CNPJ
    // -----------------------------------------------------------------------
    static boolean isCnpjValid(String cnpj) {
        if (cnpj.chars().distinct().count() == 1) return false;

        int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int sum = 0;
        for (int i = 0; i < 12; i++) sum += (cnpj.charAt(i) - '0') * weights1[i];
        int d1 = sum % 11 < 2 ? 0 : 11 - (sum % 11);
        if (d1 != (cnpj.charAt(12) - '0')) return false;

        sum = 0;
        for (int i = 0; i < 13; i++) sum += (cnpj.charAt(i) - '0') * weights2[i];
        int d2 = sum % 11 < 2 ? 0 : 11 - (sum % 11);
        return d2 == (cnpj.charAt(13) - '0');
    }
}
