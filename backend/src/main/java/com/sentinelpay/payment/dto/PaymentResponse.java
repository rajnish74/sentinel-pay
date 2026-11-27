package com.sentinelpay.payment.dto;

import com.sentinelpay.payment.entity.PaymentStatus;

import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
    UUID id,
    Long amount,
    String currency,
    PaymentStatus status,
    String clientReferenceId,
    Instant createdAt
) {
}
