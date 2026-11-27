package com.sentinelpay.merchant.mapper;

import com.sentinelpay.merchant.dto.CreateMerchantRequest;
import com.sentinelpay.merchant.dto.MerchantResponse;
import com.sentinelpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct generates the implementation at compile time.
 * componentModel = "spring" - matlab generated class @Component hoga
 */
@Mapper(componentModel = "spring")
public interface MerchantMapper {

    MerchantResponse toResponse(Merchant merchant);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "kycStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Merchant toEntity(CreateMerchantRequest request);
}
