package com.sentinelpay.ledger.repository;

import com.sentinelpay.ledger.entity.AccountType;
import com.sentinelpay.ledger.entity.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, UUID> {
    Optional<LedgerAccount> findByMerchantIdAndAccountTypeAndDeletedAtIsNull(UUID merchantId, AccountType accountType);
    Optional<LedgerAccount> findByMerchantIsNullAndAccountTypeAndDeletedAtIsNull(AccountType accountType);
}
