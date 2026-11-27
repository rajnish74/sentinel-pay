package com.sentinelpay.ledger.entity;

public enum AccountType {
    MERCHANT_RECEIVABLE, // Merchant ko milne wala paisa
    PLATFORM_REVENUE,    // Hamara commission
    CUSTOMER_ESCROW      // Customer ka hold kiya hua paisa
}
