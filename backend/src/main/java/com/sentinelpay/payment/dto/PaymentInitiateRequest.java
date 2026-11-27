package com.sentinelpay.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PaymentInitiateRequest(

    @NotNull(message = "Amount is required")
    @Min(value = 100, message = "Minimum payment amount is 100 paise (Rs 1)")
    Long amount,

    String currency,

    String clientReferenceId // Optional (merchant's order ID)
) {
}
