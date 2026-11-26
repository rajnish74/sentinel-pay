package com.sentinelpay.merchant.repository;

import com.sentinelpay.merchant.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA - method names se queries auto-generate hoti hain.
 *
 * findByIdAndDeletedAtIsNull - yeh custom method hai. Kyun? Kyunki
 * findById() soft-deleted records bhi return karta hai. Payments mein
 * we never want deleted merchants to appear - so always filter.
 */
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

    Optional<Merchant> findByIdAndDeletedAtIsNull(UUID id);

    Optional<Merchant> findByBusinessEmailAndDeletedAtIsNull(String email);

    boolean existsByBusinessEmailAndDeletedAtIsNull(String email);
}
