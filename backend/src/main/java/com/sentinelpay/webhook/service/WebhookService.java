package com.sentinelpay.webhook.service;

import com.sentinelpay.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final RestTemplate restTemplate;

    @Async
    public void sendPaymentCapturedEvent(Payment payment) {
        String webhookUrl = payment.getMerchant().getWebhookUrl();

        if (webhookUrl == null || webhookUrl.isEmpty()) {
            log.info("No webhook URL configured for merchant: {}", payment.getMerchant().getId());
            return;
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("event", "payment.captured");
        payload.put("paymentId", payment.getId());
        payload.put("amount", payment.getAmount());
        payload.put("currency", payment.getCurrency());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(webhookUrl, request, String.class);
            log.info("Webhook sent successfully to {}", webhookUrl);
        } catch (Exception e) {
            log.error("Failed to send webhook to {}: {}", webhookUrl, e.getMessage());
        }
    }
}
