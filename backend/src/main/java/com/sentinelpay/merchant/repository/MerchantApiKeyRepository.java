package com.sentinelpay.merchant.repository;

import com.sentinelpay.merchant.entity.MerchantApiKey;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MerchantApiKeyRepository extends JpaRepository<MerchantApiKey, UUID> {

    List<MerchantApiKey> findByMerchantIdAndDeletedAtIsNull(UUID merchantId);

    @EntityGraph(attributePaths = "merchant")
    Optional<MerchantApiKey> findByKeyIdAndIsActiveTrue(String keyId);

}
