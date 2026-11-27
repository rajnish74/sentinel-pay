package com.sentinelpay.ledger.entity;

import com.sentinelpay.merchant.entity.BaseEntity;
import com.sentinelpay.merchant.entity.Merchant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ledger_accounts")
@Getter
@Setter
@NoArgsConstructor
public class LedgerAccount extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 30)
    private AccountType accountType;
}
