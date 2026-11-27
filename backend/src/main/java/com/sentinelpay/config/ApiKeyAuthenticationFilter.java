package com.sentinelpay.config;

import com.sentinelpay.merchant.entity.MerchantApiKey;
import com.sentinelpay.merchant.repository.MerchantApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final MerchantApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String fullApiKey = authHeader.substring(7).trim(); // .trim() lagaya hai taaki extra space na ho
            log.info("Filter triggered. Key received: {}", fullApiKey);

            String[] parts = fullApiKey.split("\\.");

            if (parts.length == 2) {
                String keyId = parts[0];
                String rawSecret = parts[1];
                log.info("Looking up KeyId: {}", keyId);

                Optional<MerchantApiKey> apiKeyOpt = apiKeyRepository.findByKeyIdAndIsActiveTrue(keyId);

                if (apiKeyOpt.isPresent()) {
                    MerchantApiKey apiKey = apiKeyOpt.get();
                    log.info("Key found in DB. Verifying secret...");

                    if (passwordEncoder.matches(rawSecret, apiKey.getKeySecretHash())) {
                        log.info("Secret MATCHED. Setting authentication context.");
                        UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                apiKey.getMerchant(),
                                null,
                                Collections.emptyList()
                            );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        log.error("Secret MISMATCH for keyId: {}", keyId);
                    }
                } else {
                    log.error("Key not found in DB or is inactive: {}", keyId);
                }
            } else {
                log.error("Invalid key format (missing dot): {}", fullApiKey);
            }
        }

        filterChain.doFilter(request, response);
    }
}
