-- =====================================================================
-- SentinelPay V5 - Refunds Schema
-- Date: 2026-11-27
-- =====================================================================

CREATE TABLE refunds (
                       id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       payment_id          UUID            NOT NULL,

                       amount              BIGINT          NOT NULL, -- Amount in paise
                       reason              VARCHAR(255),

                       status              VARCHAR(20)     NOT NULL, -- PENDING, SUCCESS, FAILED

                       created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                       updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                       deleted_at          TIMESTAMPTZ,

                       CONSTRAINT fk_refund_payment FOREIGN KEY (payment_id) REFERENCES payments(id),
                       CONSTRAINT chk_refund_amount CHECK (amount > 0)
);

CREATE INDEX idx_refunds_payment ON refunds(payment_id);
