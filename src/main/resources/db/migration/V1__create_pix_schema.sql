-- V1__create_pix_schema.sql

-- Tabela principal de pagamentos
CREATE TABLE pix_payment (
    id              UUID PRIMARY KEY,
    end_to_end_id   VARCHAR(35)    NOT NULL UNIQUE,
    payer_key       VARCHAR(77)    NOT NULL,
    payer_key_type  VARCHAR(20)    NOT NULL,
    receiver_key    VARCHAR(77)    NOT NULL,
    receiver_key_type VARCHAR(20)  NOT NULL,
    receiver_ispb   VARCHAR(8),
    receiver_name   VARCHAR(200),
    receiver_document VARCHAR(20),
    amount          NUMERIC(15,2)  NOT NULL,
    description     VARCHAR(140),
    status          VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    type            VARCHAR(30)    NOT NULL,
    tx_id           VARCHAR(35),
    retry_count     INT            NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_pix_payment_status     ON pix_payment(status);
CREATE INDEX idx_pix_payment_created_at ON pix_payment(created_at);

-- Outbox Pattern: eventos a serem publicados no Kafka
CREATE TABLE outbox_event (
    id              UUID PRIMARY KEY,
    aggregate_id    VARCHAR(36)    NOT NULL,
    aggregate_type  VARCHAR(50)    NOT NULL,
    event_type      VARCHAR(100)   NOT NULL,
    payload         TEXT           NOT NULL,
    topic           VARCHAR(200)   NOT NULL,
    status          VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    retry_count     INT            NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    processed_at    TIMESTAMPTZ
);

CREATE INDEX idx_outbox_status      ON outbox_event(status);
CREATE INDEX idx_outbox_created_at  ON outbox_event(created_at) WHERE status = 'PENDING';

-- Webhooks: cada merchant registra sua callbackUrl aqui
CREATE TABLE webhook_registration (
    id              UUID PRIMARY KEY,
    merchant_id     VARCHAR(100)   NOT NULL UNIQUE,
    callback_url    VARCHAR(500)   NOT NULL,
    secret          VARCHAR(200)   NOT NULL,
    active          BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

-- Histórico de entregas: cada tentativa de POST ao merchant
CREATE TABLE webhook_delivery (
    id                       UUID PRIMARY KEY,
    webhook_registration_id  UUID           NOT NULL REFERENCES webhook_registration(id),
    end_to_end_id            VARCHAR(35)    NOT NULL,
    event_type               VARCHAR(50)    NOT NULL,
    payload                  TEXT           NOT NULL,
    status                   VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    attempt_count            INT            NOT NULL DEFAULT 0,
    next_retry_at            TIMESTAMPTZ,
    last_attempt_at          TIMESTAMPTZ,
    last_http_status         INT,
    last_error_message       TEXT,
    created_at               TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_webhook_delivery_status       ON webhook_delivery(status);
CREATE INDEX idx_webhook_delivery_next_retry   ON webhook_delivery(next_retry_at)
    WHERE status IN ('PENDING', 'RETRYING');
