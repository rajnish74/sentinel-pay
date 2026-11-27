package com.sentinelpay.merchant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Merchant API Keys for server-to-server authentication.
 *
 * Security rule: We NEVER store the raw secret. We only store the hash.
 * The prefix is stored just so the merchant can identify the key on the dashboard
 * (e.g., "sk_test_abc...") without exposing the whole thing.
 */
@Entity
@Table(name = "merchant_api_keys")
@Getter
@Setter
@NoArgsConstructor
public class MerchantApiKey extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(name = "key_id", nullable = false, unique = true, length = 50)
    private String keyId;

    @Column(name = "key_secret_hash", nullable = false, length = 255)
    private String keySecretHash;

    @Column(name = "key_prefix", nullable = false, length = 20)
    private String keyPrefix;

    @Column(name = "environment", nullable = false, length = 10)
    private String environment;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;
}
