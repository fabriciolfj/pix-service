package com.payment.models;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class PixPayment {

    private UUID id;
    private String endToEndId;
    private PixKey payerKey;
    private PixKey receiverKey;
    private BigDecimal amount;
    private String description;
    private PixStatus status;
    private PixPaymentType type;
    private Instant createdAt;
    private Instant updatedAt;
    private String ispb;
    private String txId;
    private int retryCount;

    public static PixPayment create(PixKey payerKey, PixKey receiverKey,
                                    BigDecimal amount, String description,
                                    PixPaymentType type, String txId) {
        return PixPayment.builder()
                .id(UUID.randomUUID())
                .endToEndId(generateEndToEndId())
                .payerKey(payerKey)
                .receiverKey(receiverKey)
                .amount(amount)
                .description(description)
                .status(PixStatus.PENDING)
                .type(type)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .retryCount(0)
                .txId(txId)
                .build();
    }

    public String getReceiveKey() {
        return this.receiverKey.getKey();
    }

    public PixPayment toProcessing() {
        validateTransition(PixStatus.PROCESSING);
        return toBuilder().status(PixStatus.PROCESSING).updatedAt(Instant.now()).build();
    }

    public PixPayment toSettled(String ispb) {
        validateTransition(PixStatus.SETTLED);
        return toBuilder().status(PixStatus.SETTLED).ispb(ispb).updatedAt(Instant.now()).build();
    }

    public PixPayment toRejected(String reason) {
        validateTransition(PixStatus.REJECTED);
        return toBuilder().status(PixStatus.REJECTED).description(reason).updatedAt(Instant.now()).build();
    }

    public PixPayment incrementRetry() {
        return toBuilder().retryCount(this.retryCount + 1).updatedAt(Instant.now()).build();
    }

    public boolean canRetry() {
        return this.retryCount < 3 && this.status == PixStatus.PROCESSING;
    }

    public boolean isCompleted() {
        return this.status == PixStatus.SETTLED || this.status == PixStatus.REJECTED;
    }

    public PixPayment validateSameOwnerTransfer() {
        String payerDoc    = payerKey.getOwnerDocument();
        String receiverDoc = receiverKey.getOwnerDocument();
        String payerIspb   = payerKey.getBankIspb();
        String receiverIspb = receiverKey.getBankIspb();

        if (payerDoc != null && payerDoc.equals(receiverDoc)
                && payerIspb != null && payerIspb.equals(receiverIspb)) {
            throw new PixBusinessException("PIX_005",
                    "Não é permitido transferir para si mesmo na mesma instituição.");
        }

        return this;
    }

    private void validateTransition(PixStatus target) {
        if (!this.status.canTransitionTo(target)) {
            throw new IllegalStateException(
                    "Transição inválida: %s -> %s para pagamento %s"
                            .formatted(this.status, target, this.id));
        }
    }

    public static String generateEndToEndId() {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 11).toUpperCase();
        return "E00000000" + ts + rand;
    }
}
