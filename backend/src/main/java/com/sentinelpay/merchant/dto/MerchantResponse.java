package com.sentinelpay.merchant.dto;

import com.sentinelpay.merchant.entity.KycStatus;
import com.sentinelpay.merchant.entity.MerchantStatus;

import java.time.Instant;
import java.util.UUID;

/**

 * Interview note: "Records are perfect for DTOs because they're
 * immutable and concise. For entities I use classes because JPA
 * needs no-arg constructor + setters for proxying."
 */
public record MerchantResponse(
    UUID id,
    String businessName,
    String businessEmail,
    String businessPhone,
    String websiteUrl,
    MerchantStatus status,
    KycStatus kycStatus,
    String panNumber,
    String gstNumber,
    Instant createdAt,
    Instant updatedAt
) {
}
