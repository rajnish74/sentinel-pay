-- =====================================================================
-- SentinelPay V4 - Double Entry Ledger
-- Date: 2026-11-27
-- =====================================================================

-- 1. Ledger Accounts (Har merchant ka ek account, platform ka ek account)
CREATE TABLE ledger_accounts (
                               id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               merchant_id         UUID,            -- Null matlab platform ka account
                               account_type        VARCHAR(30)      NOT NULL, -- 'MERCHANT_RECEIVABLE', 'PLATFORM_REVENUE', 'CUSTOMER_ESCROW'

  -- Balance yahan store nahi karte. Balance = Sum of entries.
  -- Isse performance issue ho sakta hai future mein, but correctness guaranteed hai.
                               created_at          TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
                               updated_at          TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
                               deleted_at          TIMESTAMPTZ,

                               CONSTRAINT fk_ledger_account_merchant FOREIGN KEY (merchant_id) REFERENCES merchants(id)
);

-- 2. Ledger Entries (The actual money movement)
CREATE TABLE ledger_entries (
                              id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              account_id          UUID             NOT NULL,
                              payment_id          UUID             NOT NULL, -- Kis payment ki wajah se entry hui
                              entry_type          VARCHAR(10)      NOT NULL, -- 'DEBIT' or 'CREDIT'
                              amount              BIGINT           NOT NULL, -- In paise

                              created_at          TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
                              updated_at          TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
                              deleted_at          TIMESTAMPTZ,

                              CONSTRAINT fk_ledger_entry_account FOREIGN KEY (account_id) REFERENCES ledger_accounts(id),
                              CONSTRAINT fk_ledger_entry_payment FOREIGN KEY (payment_id) REFERENCES payments(id),
                              CONSTRAINT chk_entry_type CHECK (entry_type IN ('DEBIT', 'CREDIT')),
                              CONSTRAINT chk_entry_amount CHECK (amount > 0)
);

CREATE INDEX idx_ledger_entries_account ON ledger_entries(account_id);
CREATE INDEX idx_ledger_entries_payment ON ledger_entries(payment_id);
