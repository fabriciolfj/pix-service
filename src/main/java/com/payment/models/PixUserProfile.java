package com.payment.models;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

/**
 * Perfil regulatório do pagador.
 * Armazena limites configurados pelo usuário e flags de controle do BACEN.
 */
@Getter
@Builder
public class PixUserProfile {

    private String document;            // CPF ou CNPJ
    private boolean legalEntity;        // true = PJ, false = PF

    // Limite noturno (20h-6h) — padrão R$ 1.000 para PF
    private BigDecimal nightTransactionLimit;

    // True se o usuário solicitou aumento do limite noturno (válido por 24h)
    private boolean nightLimitIncreaseRequested;
    private Instant nightLimitIncreaseExpiresAt;

    // Limite diurno customizado pelo usuário (pode reduzir abaixo do padrão)
    private BigDecimal customDayLimit;

    // Bloqueio temporário por suspeita de fraude
    private boolean blocked;
    private String blockReason;

    // -----------------------------------------------------------------------
    // Regras de limite noturno
    // -----------------------------------------------------------------------

    /**
     * Verifica se o aumento do limite noturno ainda está vigente.
     * Conforme BACEN: o aumento só é efetivo 24h após a solicitação.
     */
    public boolean hasNightLimitIncrease() {
        if (!nightLimitIncreaseRequested) return false;
        if (nightLimitIncreaseExpiresAt == null) return false;
        return Instant.now().isBefore(nightLimitIncreaseExpiresAt);
    }

    public Optional<BigDecimal> getCustomDayLimit() {
        return Optional.ofNullable(customDayLimit);
    }

    public BigDecimal getEffectiveNightLimit() {
        return hasNightLimitIncrease()
                ? nightTransactionLimit       // limite aumentado pelo usuário
                : PixBusinessRules.NIGHT_LIMIT_PF; // padrão BACEN: R$ 1.000
    }

    public void validateNotBlocked() {
        if (blocked) {
            throw new PixBusinessException("PIX_USR_001",
                    "Conta bloqueada para operações Pix. Motivo: " + blockReason);
        }
    }

    public static PixUserProfile defaultPf(String cpf) {
        return PixUserProfile.builder()
                .document(cpf)
                .legalEntity(false)
                .nightTransactionLimit(PixBusinessRules.NIGHT_LIMIT_PF)
                .nightLimitIncreaseRequested(false)
                .blocked(false)
                .build();
    }

    public static PixUserProfile defaultPj(String cnpj) {
        return PixUserProfile.builder()
                .document(cnpj)
                .legalEntity(true)
                .blocked(false)
                .build();
    }
}
