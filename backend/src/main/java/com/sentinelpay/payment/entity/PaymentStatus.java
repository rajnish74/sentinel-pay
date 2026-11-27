package com.sentinelpay.payment.entity;

public enum PaymentStatus {
    INITIATED,   // Request aayi, abhi process nahi hui
    AUTHORIZED,  // Bank ne paisa hold kiya hai
    CAPTURED,    // Paisa kat gaya (Success)
    FAILED,      // Fail ho gayi
    CANCELLED    // User ne cancel kar diya
}
