-- =====================================================================
-- SentinelPay V6 - Update Payment Status Constraint for Refunds

--

-- =====================================================================


ALTER TABLE payments DROP CONSTRAINT IF EXISTS chk_payment_status;


ALTER TABLE payments ADD CONSTRAINT chk_payment_status
  CHECK (status IN (
                    'INITIATED',
                    'AUTHORIZED',
                    'CAPTURED',
                    'FAILED',
                    'CANCELLED',
                    'PARTIALLY_REFUNDED',
                    'REFUNDED'
    ));
