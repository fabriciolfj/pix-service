package com.payment.models;

public enum PixPaymentType {
    TRANSFER,       // transferência simples
    PAYMENT,        // pagamento de cobrança (QR Code estático)
    DYNAMIC_PAYMENT // pagamento de cobrança (QR Code dinâmico)
}
