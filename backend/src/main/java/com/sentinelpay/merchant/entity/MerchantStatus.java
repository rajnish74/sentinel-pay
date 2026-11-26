package com.sentinelpay.merchant.entity;
/**
 * Lifecycle of a merchant on SentinelPay.
 *
 * PENDING     -> just signed up, KYC not done
 * ACTIVE      -> can accept payments
 * SUSPENDED   -> compliance issue, can't transact
 * CLOSED      -> merchant requested closure
 */
public enum MerchantStatus {
    PENDING,
    ACTIVE,
    SUSPENDED,
    CLOSED
}
