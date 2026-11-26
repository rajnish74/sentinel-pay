-- =====================================================================
-- SentinelPay V1 - Initial Schema (Phase 1)
-- Author: [Your Name]
-- Date: 2026-11-25
--
-- Yeh file Flyway automatically run karegi on app startup.
-- Rule: Flyway files KABHI edit nahi karte ek baar run ho gayi toh.
-- Naya change = naya V2__... file. Yeh maine early samajh liya, warna
-- 6 mahine baad bada problem hota.
-- =====================================================================

-- UUID generation ke liye
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =====================================================================
-- TABLE: merchants
-- Har merchant jo platform pe register karta hai
-- =====================================================================
CREATE TABLE merchants (
                         id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         business_name       VARCHAR(255)    NOT NULL,
                         business_email      VARCHAR(255)    NOT NULL,
                         business_phone      VARCHAR(20)     NOT NULL,
                         website_url         VARCHAR(500),

  -- Status lifecycle: PENDING -> ACTIVE -> SUSPENDED
                         status              VARCHAR(20)     NOT NULL DEFAULT 'PENDING',

  -- KYC info (Phase 3 mein full KYC, abhi sirf basic)
                         kyc_status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
                         pan_number          VARCHAR(10),
                         gst_number          VARCHAR(15),

  -- Timestamps
                         created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                         updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                         deleted_at          TIMESTAMPTZ,            -- soft delete

  -- Constraints
                         CONSTRAINT uq_merchant_email UNIQUE (business_email)
);

-- Indexes - queries ke liye
CREATE INDEX idx_merchants_status ON merchants(status) WHERE deleted_at IS NULL;
CREATE INDEX idx_merchants_email ON merchants(business_email) WHERE deleted_at IS NULL;

COMMENT ON TABLE merchants IS 'Registered merchants on SentinelPay platform';
COMMENT ON COLUMN merchants.status IS 'PENDING, ACTIVE, SUSPENDED, CLOSED';
COMMENT ON COLUMN merchants.kyc_status IS 'PENDING, IN_REVIEW, VERIFIED, REJECTED';

-- =====================================================================
-- TABLE: merchant_api_keys
-- Har merchant multiple API keys rakh sakta hai (dev, prod, etc.)
-- NOTE: key_secret ka sirf HASH store karte hain, plain never
-- (same concept as password - maine yeh security interview mein poocha tha)
-- =====================================================================
CREATE TABLE merchant_api_keys (
                                 id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 merchant_id         UUID            NOT NULL,
                                 key_id              VARCHAR(50)     NOT NULL,        -- public identifier
                                 key_secret_hash     VARCHAR(255)    NOT NULL,        -- bcrypt hash
                                 key_prefix          VARCHAR(20)     NOT NULL,        -- for UI display: sk_test_abc...
                                 environment         VARCHAR(10)     NOT NULL,        -- TEST or LIVE

                                 is_active           BOOLEAN         NOT NULL DEFAULT TRUE,
                                 last_used_at        TIMESTAMPTZ,
                                 expires_at          TIMESTAMPTZ,

                                 created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                 updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                 deleted_at          TIMESTAMPTZ,

                                 CONSTRAINT fk_api_key_merchant FOREIGN KEY (merchant_id)
                                   REFERENCES merchants(id) ON DELETE RESTRICT,
                                 CONSTRAINT uq_api_key_id UNIQUE (key_id),
                                 CONSTRAINT chk_environment CHECK (environment IN ('TEST', 'LIVE'))
);

CREATE INDEX idx_api_keys_merchant ON merchant_api_keys(merchant_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_api_keys_key_id ON merchant_api_keys(key_id) WHERE is_active = TRUE;

COMMENT ON TABLE merchant_api_keys IS 'API keys for merchant authentication';

-- =====================================================================
-- TABLE: platform_users
-- Ye log jo dashboard login karte hain (merchants nahi - separate)
-- Roles: SUPER_ADMIN, MERCHANT_ADMIN, FRAUD_ANALYST, etc.
-- =====================================================================
CREATE TABLE platform_users (
                              id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              email               VARCHAR(255)    NOT NULL,
                              password_hash       VARCHAR(255)    NOT NULL,
                              full_name           VARCHAR(255)    NOT NULL,

  -- Role - Phase 1 mein simple, Phase 3 mein separate roles-permissions table
                              role                VARCHAR(30)     NOT NULL,

  -- Optional merchant link (null for SUPER_ADMIN / FRAUD_ANALYST)
                              merchant_id         UUID,

                              is_active           BOOLEAN         NOT NULL DEFAULT TRUE,
                              last_login_at       TIMESTAMPTZ,

                              created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                              updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                              deleted_at          TIMESTAMPTZ,

                              CONSTRAINT uq_platform_user_email UNIQUE (email),
                              CONSTRAINT fk_platform_user_merchant FOREIGN KEY (merchant_id)
                                REFERENCES merchants(id) ON DELETE SET NULL,
                              CONSTRAINT chk_user_role CHECK (
                                role IN ('SUPER_ADMIN', 'OPERATIONS_ADMIN', 'FRAUD_ANALYST',
                                         'MERCHANT_ADMIN', 'MERCHANT_DEVELOPER',
                                         'MERCHANT_FINANCE', 'MERCHANT_SUPPORT')
                                )
);

CREATE INDEX idx_users_email ON platform_users(email) WHERE deleted_at IS NULL;
CREATE INDEX idx_users_merchant ON platform_users(merchant_id) WHERE deleted_at IS NULL;

COMMENT ON TABLE platform_users IS 'Users who can log into the dashboard';

