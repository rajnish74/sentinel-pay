package com.sentinelpay.merchant.dto;

import java.time.Instant;
import java.util.UUID;

public record ApiKeyResponse(
    UUID id,
    String keyId,
    String keyPrefix,
    String environment,
    boolean isActive,
    Instant createdAt
) {
}
