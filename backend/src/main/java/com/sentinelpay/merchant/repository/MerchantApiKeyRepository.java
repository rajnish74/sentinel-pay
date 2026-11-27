package com.sentinelpay.merchant.repository;

import com.sentinelpay.merchant.entity.MerchantApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MerchantApiKeyRepository extends JpaRepository<MerchantApiKey, UUID> {

    List<MerchantApiKey> findByMerchantIdAndDeletedAtIsNull(UUID merchantId);
}
