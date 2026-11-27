package com.sentinelpay.payment.service;

import com.sentinelpay.common.exception.ResourceNotFoundException;
import com.sentinelpay.merchant.entity.Merchant;
import com.sentinelpay.merchant.repository.MerchantRepository;
import com.sentinelpay.payment.dto.PaymentInitiateRequest;
import com.sentinelpay.payment.dto.PaymentResponse;
import com.sentinelpay.payment.entity.Payment;
import com.sentinelpay.payment.entity.PaymentStatus;
import com.sentinelpay.payment.mapper.PaymentMapper;
import com.sentinelpay.payment.repository.PaymentRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MerchantRepository merchantRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentResponse initiatePayment(UUID merchantId, String idempotencyKey, PaymentInitiateRequest request){

//        Idempotency check
        Optional<Payment> existingPayment = paymentRepository.findByMerchantIdAndIdempotencyKey(merchantId, idempotencyKey);

        if (existingPayment.isPresent()) {
            return paymentMapper.toResponse(existingPayment.get());
        }

        Merchant merchant = merchantRepository.findByIdAndDeletedAtIsNull(merchantId)
            .orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));

        Payment payment = new Payment();
        payment.setMerchant(merchant);
        payment.setAmount(request.amount());
        payment.setCurrency(request.currency() != null ? request.currency().toUpperCase() : "INR");
        payment.setStatus(PaymentStatus.INITIATED);
        payment.setIdempotencyKey(idempotencyKey);
        payment.setClientReferenceId(request.clientReferenceId());

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);

    }


}
