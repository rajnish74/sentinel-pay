package com.sentinelpay.payment.controller;

import com.sentinelpay.payment.dto.PaymentInitiateRequest;
import com.sentinelpay.payment.dto.PaymentResponse;
import com.sentinelpay.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Initiate a new payment")
    public ResponseEntity<PaymentResponse> initiatePayment(
        // In reality, merchantId would come from the API Key authentication context.
        // For now, we pass it as a header for testing purposes.
        @RequestHeader("X-Merchant-Id") UUID merchantId,

        // Mandatory header for idempotency (like Stripe)
        @Parameter(description = "Unique key to prevent duplicate payments")
        @RequestHeader("Idempotency-Key") String idempotencyKey,

        @Valid @RequestBody PaymentInitiateRequest request) {

        PaymentResponse response = paymentService.initiatePayment(merchantId, idempotencyKey, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{paymentId}/capture")
    @Operation(summary = "Capture a payment (triggers ledger entries)")
    public ResponseEntity<PaymentResponse> capturePayment(@PathVariable UUID paymentId) {
        PaymentResponse response = paymentService.capturePayment(paymentId);
        return ResponseEntity.ok(response);
    }
}
