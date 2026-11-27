package com.sentinelpay.payment.service;

import com.sentinelpay.common.exception.ResourceNotFoundException;
import com.sentinelpay.ledger.service.LedgerService;
import com.sentinelpay.payment.dto.RefundRequest;
import com.sentinelpay.payment.dto.RefundResponse;
import com.sentinelpay.payment.entity.*;
import com.sentinelpay.payment.repository.PaymentRepository;
import com.sentinelpay.payment.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final LedgerService ledgerService;

    @Transactional
    public RefundResponse processRefund(UUID merchantId, UUID paymentId, RefundRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        if (!payment.getMerchant().getId().equals(merchantId)) {
            throw new ResourceNotFoundException("Payment", paymentId);
        }

        if (payment.getStatus() != PaymentStatus.CAPTURED && payment.getStatus() != PaymentStatus.PARTIALLY_REFUNDED) {
            throw new IllegalStateException("Only CAPTURED or PARTIALLY_REFUNDED payments can be refunded.");
        }

        Long totalRefunded = payment.getRefundedAmount();
        if (totalRefunded + request.amount() > payment.getAmount()) {
            throw new IllegalArgumentException("Refund amount exceeds remaining payment amount.");
        }

        // 3. Create Refund Record
        Refund refund = new Refund();
        refund.setPayment(payment);
        refund.setAmount(request.amount());
        refund.setReason(request.reason());
        refund.setStatus(RefundStatus.SUCCESS); // Assume success immediately for MVP
        Refund savedRefund = refundRepository.save(refund);

        // 4. Update Payment Status & Refunded Amount
        payment.setRefundedAmount(totalRefunded + request.amount());
        if (payment.getRefundedAmount().equals(payment.getAmount())) {
            payment.setStatus(PaymentStatus.REFUNDED);
        } else {
            payment.setStatus(PaymentStatus.PARTIALLY_REFUNDED);
        }
        paymentRepository.save(payment);

        // 5. Record in Ledger (Reverse Entry)
        ledgerService.recordPaymentRefund(payment, request.amount());

        return new RefundResponse(
            savedRefund.getId(),
            paymentId,
            savedRefund.getAmount(),
            savedRefund.getReason(),
            savedRefund.getStatus(),
            savedRefund.getCreatedAt()
        );
    }
}
