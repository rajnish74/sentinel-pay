package com.sentinelpay.payment.mapper;

import com.sentinelpay.payment.dto.PaymentResponse;
import com.sentinelpay.payment.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}
