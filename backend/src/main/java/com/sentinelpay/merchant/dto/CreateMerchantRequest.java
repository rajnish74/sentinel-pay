package com.sentinelpay.merchant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateMerchantRequest(
    @NotBlank(message = "Business name is required")
    @Size(min = 2, max = 255, message = "Business name must be between 2 and 255 characters")
    String businessName,

    @NotBlank(message = "Business email is required")
    @Email(message = "Invalid email format")
    String businessEmail,

    @NotBlank(message = "Business phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone must be in international format (e.g., +919999999999)")
    String businessPhone,

    @Size(max = 500, message = "Website URL too long")
    String websiteUrl,

    // PAN card validation (10 chars: 5 letters, 4 numbers, 1 letter)
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid PAN format")
    String panNumber,

    // GSTIN validation (15 chars)
    @Size(min = 15, max = 15, message = "GST number must be exactly 15 characters")
    String gstNumber
) {
}
