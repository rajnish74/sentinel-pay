package com.sentinelpay.merchant.controller;

import com.sentinelpay.merchant.dto.ApiKeyCreateResponse;
import com.sentinelpay.merchant.dto.ApiKeyResponse;
import com.sentinelpay.merchant.dto.CreateMerchantRequest;
import com.sentinelpay.merchant.dto.MerchantResponse;
import com.sentinelpay.merchant.service.ApiKeyService;
import com.sentinelpay.merchant.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
@Tag(name = "Merchants", description = "Merchant management endpoints")
public class MerchantController {

    private final MerchantService merchantService;
    private final ApiKeyService apiKeyService;

    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponse> getMerchant(@PathVariable UUID id) {
        MerchantResponse response = merchantService.getMerchantById(id);
        return ResponseEntity.ok(response);

    }

    @PostMapping
    @Operation(summary = "Create a new merchant")
    public ResponseEntity<MerchantResponse> createMerchant(@Valid @RequestBody CreateMerchantRequest request){
        MerchantResponse response = merchantService.createMerchant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{merchantId}/api-keys")
    @Operation(summary = "Generate a new API Key for a merchant")
    public ResponseEntity<ApiKeyCreateResponse> generateApiKey(@PathVariable UUID merchantId,
                                                               @RequestParam(defaultValue = "Test") String environment){
        ApiKeyCreateResponse response = apiKeyService.generateApiKey(merchantId, environment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{merchantId}/api-keys")
    @Operation(summary = "List all API Keys for a merchant (Secrets are never returned)")
    public ResponseEntity<List<ApiKeyResponse>> listApiKeys(@PathVariable UUID merchantId) {
        List<ApiKeyResponse> response = apiKeyService.getMerchantApiKeys(merchantId);
        return ResponseEntity.ok(response);
    }
}
