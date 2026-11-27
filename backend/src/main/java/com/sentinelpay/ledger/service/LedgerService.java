package com.sentinelpay.ledger.service;

import com.sentinelpay.ledger.entity.AccountType;
import com.sentinelpay.ledger.entity.EntryType;
import com.sentinelpay.ledger.entity.LedgerAccount;
import com.sentinelpay.ledger.entity.LedgerEntry;
import com.sentinelpay.ledger.repository.LedgerAccountRepository;
import com.sentinelpay.ledger.repository.LedgerEntryRepository;
import com.sentinelpay.merchant.entity.Merchant;
import com.sentinelpay.merchant.repository.MerchantRepository;
import com.sentinelpay.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerAccountRepository accountRepository;
    private final LedgerEntryRepository entryRepository;
    private final MerchantRepository merchantRepository;

    /**
     * Record a double-entry transaction.
     *
     * Example: Payment captured.
     * Debit: Merchant Receivable (+ Amount)
     * Credit: Customer Escrow (- Amount)
     *
     * @Transactional ensures dono entries ek sath save ho ya dono fail ho.
     */
    @Transactional
    public void recordPaymentCapture(Payment payment) {
        //  Get or Create Accounts
        LedgerAccount merchantAccount = getOrCreateMerchantAccount(payment.getMerchant());
        LedgerAccount escrowAccount = getOrCreatePlatformAccount(AccountType.CUSTOMER_ESCROW);


        //  Debit Merchant Account (Paisa unko mila)
        LedgerEntry debitEntry = new LedgerEntry();
        debitEntry.setAccount(merchantAccount);
        debitEntry.setPayment(payment);
        debitEntry.setEntryType(EntryType.DEBIT);
        debitEntry.setAmount(payment.getAmount());

        // Credit Escrow Account (Paisa hold se nikla)
        LedgerEntry creditEntry = new LedgerEntry();
        creditEntry.setAccount(escrowAccount);
        creditEntry.setPayment(payment);
        creditEntry.setEntryType(EntryType.CREDIT);
        creditEntry.setAmount(payment.getAmount());

        entryRepository.save(debitEntry);
        entryRepository.save(creditEntry);
    }

    private LedgerAccount getOrCreateMerchantAccount(Merchant merchant) {
        return accountRepository.findByMerchantIdAndAccountTypeAndDeletedAtIsNull(merchant.getId(), AccountType.MERCHANT_RECEIVABLE)
            .orElseGet(() -> {
                LedgerAccount acc = new LedgerAccount();
                acc.setMerchant(merchant);
                acc.setAccountType(AccountType.MERCHANT_RECEIVABLE);
                return accountRepository.save(acc);
            });
    }

    private LedgerAccount getOrCreatePlatformAccount(AccountType type) {
        return accountRepository.findByMerchantIsNullAndAccountTypeAndDeletedAtIsNull(type)
            .orElseGet(() -> {
                LedgerAccount acc = new LedgerAccount();
                acc.setAccountType(type);
                return accountRepository.save(acc);
            });
    }
}
