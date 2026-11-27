package com.sentinelpay.payment.controller;

import com.sentinelpay.merchant.entity.Merchant;
import com.sentinelpay.payment.dto.PaymentInitiateRequest;
import com.sentinelpay.payment.dto.PaymentResponse;
import com.sentinelpay.payment.dto.RefundRequest;
import com.sentinelpay.payment.dto.RefundResponse;
import com.sentinelpay.payment.service.PaymentService;
import com.sentinelpay.payment.service.RefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final RefundService refundService;

    @PostMapping
    @Operation(summary = "Initiate a new payment")
    public ResponseEntity<PaymentResponse> initiatePayment(
        @AuthenticationPrincipal Merchant merchant,
        @RequestHeader("Idempotency-Key") String idempotencyKey,
        @Valid @RequestBody PaymentInitiateRequest request) {

        PaymentResponse response = paymentService.initiatePayment(merchant.getId(), idempotencyKey, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{paymentId}/capture")
    @Operation(summary = "Capture a payment (triggers ledger entries)")
    public ResponseEntity<PaymentResponse> capturePayment(
        @AuthenticationPrincipal Merchant merchant,
        @PathVariable UUID paymentId) {

        PaymentResponse response = paymentService.capturePayment(merchant.getId(), paymentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{paymentId}/refunds")
    @Operation(summary = "Refund a payment (full or partial)")
    public ResponseEntity<RefundResponse> refundPayment(
        @AuthenticationPrincipal Merchant merchant,
        @PathVariable UUID paymentId,
        @Valid @RequestBody RefundRequest request) {

        RefundResponse response = refundService.processRefund(merchant.getId(), paymentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
