package com.sentinelpay.merchant.service;

import com.sentinelpay.common.exception.ResourceNotFoundException;
import com.sentinelpay.merchant.dto.ApiKeyCreateResponse;
import com.sentinelpay.merchant.dto.ApiKeyResponse;
import com.sentinelpay.merchant.entity.Merchant;
import com.sentinelpay.merchant.entity.MerchantApiKey;
import com.sentinelpay.merchant.repository.MerchantApiKeyRepository;
import com.sentinelpay.merchant.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final MerchantApiKeyRepository merchantApiKeyRepository;
    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    @Transactional
    public ApiKeyCreateResponse generateApiKey(UUID merchantId, String environment) {
        Merchant merchant = merchantRepository.findByIdAndDeletedAtIsNull(merchantId)
            .orElseThrow(()->new ResourceNotFoundException("Merchant", merchantId));

//        generate secret
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String rawSecret = base64Encoder.encodeToString(randomBytes);

        // Key ID (e.g., key_xyz123)
        String keyId = "key_" + base64Encoder.encodeToString(randomBytes).substring(0, 8);

//        Prefix for UI (e.g., sk_test_abc...)
        String prefixType = environment.equals("TEST") ? "sk_test_" : "sk_live_";
        String keyPrefix = prefixType + rawSecret.substring(0, 4);

        String secretHash = passwordEncoder.encode(rawSecret);

//        save to db
        MerchantApiKey apiKey = new MerchantApiKey();
        apiKey.setMerchant(merchant);
        apiKey.setKeyId(keyId);
        apiKey.setKeyPrefix(keyPrefix);
        apiKey.setKeySecretHash(secretHash);
        apiKey.setEnvironment(environment);

        MerchantApiKey savedKey = merchantApiKeyRepository.save(apiKey);

        //  Return response with RAW secret (only time we do this)
        return new ApiKeyCreateResponse(
            savedKey.getId(),
            savedKey.getKeyId(),
            rawSecret,
            savedKey.getKeyPrefix(),
            savedKey.getEnvironment(),
            savedKey.getCreatedAt()
        );

    }

    @Transactional(readOnly = true)
    public List<ApiKeyResponse> getMerchantApiKeys(UUID merchantId) {
        List<MerchantApiKey> keys = merchantApiKeyRepository.findByMerchantIdAndDeletedAtIsNull(merchantId);

        return keys.stream().map(key->new ApiKeyResponse(
            key.getId(),
            key.getKeyId(),
            key.getKeyPrefix(),
            key.getEnvironment(),
            key.isActive(),
            key.getCreatedAt()
        )).collect(Collectors.toList());
    }
}
