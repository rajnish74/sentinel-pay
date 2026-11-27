ALTER TABLE payments ADD COLUMN deleted_at TIMESTAMPTZ;

COMMENT ON COLUMN payments.deleted_at IS 'Soft delete timestamp';
