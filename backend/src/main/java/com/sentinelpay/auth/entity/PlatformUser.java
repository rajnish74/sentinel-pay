package com.sentinelpay.auth.entity;

import com.sentinelpay.merchant.entity.BaseEntity;
import com.sentinelpay.merchant.entity.Merchant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "platform_users")
@Getter
@Setter
@NoArgsConstructor
public class PlatformUser extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserRole role;

    // Sirf merchant-level users ke paas merchant_id hoga (e.g., Super Admin ka null hoga)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
