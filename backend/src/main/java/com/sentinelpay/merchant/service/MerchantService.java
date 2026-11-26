package com.sentinelpay.merchant.service;

import com.sentinelpay.common.exception.ResourceNotFoundException;
import com.sentinelpay.merchant.dto.MerchantResponse;
import com.sentinelpay.merchant.entity.Merchant;
import com.sentinelpay.merchant.mapper.MerchantMapper;
import com.sentinelpay.merchant.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    public MerchantResponse getMerchantById(UUID id) {
        Merchant merchant = merchantRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(()->new ResourceNotFoundException("Merchant", id));

        return merchantMapper.toResponse(merchant);
    }
}
