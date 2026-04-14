-- ============================================================
-- ShedLock — lock distribuído para @Scheduled
--
-- Impede que múltiplas instâncias (pods K8s) do pix-payment-service
-- executem o polling SPI ao mesmo tempo, evitando:
--   - Processamento duplicado da mesma resposta do BACEN
--   - Conflito de UPDATE no pix_payment (status já atualizado)
--
-- Fluxo:
--   1. Pod A tenta adquirir o lock "spi-response-polling"
--   2. ShedLock faz INSERT/UPDATE em shedlock com lock_until = now() + lockAtMostFor
--   3. Pod B tenta o mesmo INSERT e falha (PK conflict) → skip esta rodada
--   4. Pod A termina → ShedLock atualiza lock_until = now() (libera)
--   5. Próximo pod que tentar após lock_until pode adquirir
-- ============================================================
CREATE TABLE IF NOT EXISTS shedlock (
    name       VARCHAR(64)  NOT NULL,
    lock_until TIMESTAMP(3) NOT NULL,
    locked_at  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    locked_by  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_shedlock PRIMARY KEY (name)
);
