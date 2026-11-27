-- =====================================================================
-- SentinelPay V2 - Payment Core Schema
-- Date: 2026-11-27
-- =====================================================================

CREATE TABLE payments (
                        id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        merchant_id         UUID            NOT NULL,

  -- Amount is always stored in the smallest currency unit (e.g., paise for INR)
  -- NEVER use Float/Double for money. Rs 100.50 = 10050 paise.
                        amount              BIGINT          NOT NULL,
                        currency            VARCHAR(3)      NOT NULL DEFAULT 'INR',

  -- Payment lifecycle state
                        status              VARCHAR(20)     NOT NULL,

  -- Idempotency: Unique per merchant.
  -- If a network retry happens, we check this key to prevent double charging.
                        idempotency_key     VARCHAR(100)    NOT NULL,

  -- Merchant's internal order ID (optional)
                        client_reference_id VARCHAR(100),

                        created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                        updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

                        CONSTRAINT fk_payment_merchant FOREIGN KEY (merchant_id) REFERENCES merchants(id),

  -- CRITICAL: This prevents double charging.
  -- Ek merchant ek idempotency_key sirf ek baar use kar sakta hai.
                        CONSTRAINT uq_payment_idempotency UNIQUE (merchant_id, idempotency_key),

                        CONSTRAINT chk_payment_amount CHECK (amount > 0),
                        CONSTRAINT chk_payment_status CHECK (status IN ('INITIATED', 'AUTHORIZED', 'CAPTURED', 'FAILED', 'CANCELLED'))
);

CREATE INDEX idx_payments_merchant ON payments(merchant_id);
CREATE INDEX idx_payments_status ON payments(status);

COMMENT ON TABLE payments IS 'Core payment transactions';
