package com.payment.models;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class PixBusinessRules {

    static final LocalTime NIGHT_START = LocalTime.of(20, 0);
    static final LocalTime NIGHT_END   = LocalTime.of(6, 0);
    static final BigDecimal NIGHT_LIMIT_PF       = new BigDecimal("1000.00");
    static final BigDecimal DEFAULT_DAY_LIMIT_PF = new BigDecimal("20000.00");
    static final BigDecimal DEFAULT_LIMIT_PJ     = new BigDecimal("999999999.99");
    static final BigDecimal MIN_AMOUNT           = new BigDecimal("0.01");
    static final int MAX_DESCRIPTION_LENGTH      = 140;

    public static void validate(PixPayment payment, PixUserProfile payerProfile) {
        validateAmount(payment.getAmount());
        validateNightLimit(payment, payerProfile);
        validateDailyLimit(payment, payerProfile);
        validateDescription(payment.getDescription());
        validateSameOwnerTransfer(payment);
    }

    static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(MIN_AMOUNT) < 0) {
            throw new PixBusinessException("PIX_001",
                    "Valor inválido. Mínimo permitido: R$ 0,01");
        }
    }

    static void validateNightLimit(PixPayment payment, PixUserProfile payerProfile) {
        if (payerProfile.isLegalEntity()) return;
        if (!isNightPeriod()) return;
        if (payerProfile.hasNightLimitIncrease()) return;

        if (payment.getAmount().compareTo(NIGHT_LIMIT_PF) > 0) {
            throw new PixBusinessException("PIX_002",
                    "Valor excede o limite noturno de R$ 1.000,00 (20h–6h para PF).");
        }
    }

    static void validateDailyLimit(PixPayment payment, PixUserProfile payerProfile) {
        BigDecimal limit = payerProfile.isLegalEntity()
                ? DEFAULT_LIMIT_PJ
                : payerProfile.getCustomDayLimit().orElse(DEFAULT_DAY_LIMIT_PF);

        if (payment.getAmount().compareTo(limit) > 0) {
            throw new PixBusinessException("PIX_003",
                    "Valor R$ %s excede o limite de R$ %s por transação."
                            .formatted(payment.getAmount(), limit));
        }
    }

    static void validateDescription(String description) {
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new PixBusinessException("PIX_004",
                    "Descrição excede 140 caracteres.");
        }
    }

    static void validateSameOwnerTransfer(PixPayment payment) {
        String payerDoc    = payment.getPayerKey().getOwnerDocument();
        String receiverDoc = payment.getReceiverKey().getOwnerDocument();
        String payerIspb   = payment.getPayerKey().getBankIspb();
        String receiverIspb = payment.getReceiverKey().getBankIspb();

        if (payerDoc != null && receiverDoc != null
                && payerDoc.equals(receiverDoc)
                && payerIspb != null && payerIspb.equals(receiverIspb)) {
            throw new PixBusinessException("PIX_005",
                    "Não é permitido transferir para si mesmo na mesma instituição.");
        }
    }

    public static boolean isNightPeriod() {
        LocalTime now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalTime();
        return now.isAfter(NIGHT_START) || now.isBefore(NIGHT_END);
    }
}
