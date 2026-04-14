-- Perfil regulatório do pagador Pix
CREATE TABLE user_pix_profile (
    document                       VARCHAR(20)    PRIMARY KEY,
    legal_entity                   BOOLEAN        NOT NULL DEFAULT FALSE,
    night_transaction_limit        NUMERIC(15,2)  NOT NULL DEFAULT 1000.00,
    night_limit_increase_requested BOOLEAN        NOT NULL DEFAULT FALSE,
    night_limit_increase_expires_at TIMESTAMPTZ,
    custom_day_limit               NUMERIC(15,2),
    blocked                        BOOLEAN        NOT NULL DEFAULT FALSE,
    block_reason                   TEXT,
    created_at                     TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at                     TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

-- Tabela de cobranças Pix (QR Code estático e dinâmico)
CREATE TABLE pix_charge (
    id              UUID PRIMARY KEY,
    tx_id           VARCHAR(35)    NOT NULL UNIQUE,
    merchant_key    VARCHAR(77)    NOT NULL,
    merchant_name   VARCHAR(200)   NOT NULL,
    merchant_city   VARCHAR(60)    NOT NULL,
    amount          NUMERIC(15,2),          -- null = valor em aberto
    description     VARCHAR(140),
    status          VARCHAR(20)    NOT NULL DEFAULT 'ACTIVE',
    type            VARCHAR(10)    NOT NULL,
    expires_at      TIMESTAMPTZ,
    paid_at         TIMESTAMPTZ,
    end_to_end_id   VARCHAR(35),
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_pix_charge_tx_id  ON pix_charge(tx_id);
CREATE INDEX idx_pix_charge_status ON pix_charge(status);
