package com.sentinelpay.merchant.controller;

import com.sentinelpay.merchant.dto.MerchantResponse;
import com.sentinelpay.merchant.service.MerchantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
@Tag(name = "Merchants", description = "Merchant management endpoints")
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponse> getMerchant(@PathVariable UUID id) {
        MerchantResponse response = merchantService.getMerchantById(id);
        return ResponseEntity.ok(response);

    }
}
