package com.sentinelpay.merchant.mapper;

import com.sentinelpay.merchant.dto.MerchantResponse;
import com.sentinelpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;

/**
 * MapStruct generates the implementation at compile time.
 * componentModel = "spring" - matlab generated class @Component hoga
 */
@Mapper(componentModel = "spring")
public interface MerchantMapper {

    MerchantResponse toResponse(Merchant merchant);
}
