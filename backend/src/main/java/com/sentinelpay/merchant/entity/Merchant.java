package com.sentinelpay.merchant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "merchants",
    indexes = {
        @Index(name = "idx_merchant_status", columnList = "status"),
        @Index(name = "idx_merchant_email", columnList = "business_email")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class Merchant extends BaseEntity {

    @Column(name = "business_name", nullable = false, length = 255)
    private String businessName;

    @Column(name = "business_email", nullable = false, length = 255, unique = true)
    private String businessEmail;

    @Column(name = "business_phone", nullable = false, length = 20)
    private String businessPhone;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MerchantStatus status = MerchantStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false, length = 20)
    private KycStatus kycStatus = KycStatus.PENDING;

    @Column(name = "pan_number", length = 10)
    private String panNumber;

    @Column(name = "gst_number", length = 15)
    private String gstNumber;

}
