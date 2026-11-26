-- Dev-only seed data
-- Yeh file Flyway manage NAHI karti. Manually run karte hain ya
-- application-dev.yml mein reference denge (later phase)

INSERT INTO merchants (
  id, business_name, business_email, business_phone,
  status, kyc_status
) VALUES (
           '11111111-1111-1111-1111-111111111111',
           'Demo Store Pvt Ltd',
           'demo@sentinelpay.dev',
           '+919999999999',
           'ACTIVE',
           'VERIFIED'
         );

INSERT INTO platform_users (
  email, password_hash, full_name, role, merchant_id
) VALUES
    (
      'admin@sentinelpay.dev',
      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
      'Super Admin',
      'SUPER_ADMIN',
      NULL
    ),
    (
      'merchant@sentinelpay.dev',
      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
      'Demo Merchant Admin',
      'MERCHANT_ADMIN',
      '11111111-1111-1111-1111-111111111111'
    );
