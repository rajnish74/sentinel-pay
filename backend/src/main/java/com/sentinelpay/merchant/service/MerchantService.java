package com.sentinelpay.merchant.service;

import com.sentinelpay.common.exception.DuplicateResourceException;
import com.sentinelpay.common.exception.ResourceNotFoundException;
import com.sentinelpay.merchant.dto.CreateMerchantRequest;
import com.sentinelpay.merchant.dto.MerchantResponse;
import com.sentinelpay.merchant.entity.KycStatus;
import com.sentinelpay.merchant.entity.Merchant;
import com.sentinelpay.merchant.entity.MerchantStatus;
import com.sentinelpay.merchant.mapper.MerchantMapper;
import com.sentinelpay.merchant.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    public MerchantResponse createMerchant(CreateMerchantRequest request) {
        if (merchantRepository.existsByBusinessEmailAndDeletedAtIsNull(request.businessEmail())){
            throw new DuplicateResourceException("Merchant with email already exist" + request.businessEmail());
        }

//        DTO to entity
        Merchant merchant = merchantMapper.toEntity(request);

        merchant.setStatus(MerchantStatus.PENDING);
        merchant.setKycStatus(KycStatus.PENDING);

        Merchant save = merchantRepository.save(merchant);
        return merchantMapper.toResponse(save);


    }
}
