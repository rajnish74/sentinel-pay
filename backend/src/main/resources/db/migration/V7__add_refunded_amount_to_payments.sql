ALTER TABLE payments ADD COLUMN refunded_amount BIGINT NOT NULL DEFAULT 0;

COMMENT ON COLUMN payments.refunded_amount IS 'Total amount refunded so far (in paise)';
