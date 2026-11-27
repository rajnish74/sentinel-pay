package com.sentinelpay.payment.dto;

import com.sentinelpay.payment.entity.RefundStatus;

import java.time.Instant;
import java.util.UUID;

public record RefundResponse(
    UUID id,
    UUID paymentId,
    Long amount,
    String reason,
    RefundStatus status,
    Instant createdAt
) {}
