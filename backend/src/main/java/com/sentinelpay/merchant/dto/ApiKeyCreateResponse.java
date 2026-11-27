package com.sentinelpay.merchant.dto;

import java.time.Instant;
import java.util.UUID;

public record ApiKeyCreateResponse(
    UUID id,
    String keyId,
    String keySecret,
    String keyPrefix,
    String environment,
    Instant createdAt
) {
}
