package com.sentinelpay.auth.service;

import com.sentinelpay.auth.dto.AuthRequest;
import com.sentinelpay.auth.dto.AuthResponse;
import com.sentinelpay.auth.dto.RegisterRequest;
import com.sentinelpay.auth.entity.PlatformUser;
import com.sentinelpay.auth.entity.UserRole;
import com.sentinelpay.auth.repository.PlatformUserRepository;
import com.sentinelpay.common.exception.DuplicateResourceException;
import com.sentinelpay.common.exception.ResourceNotFoundException;
import com.sentinelpay.merchant.entity.KycStatus;
import com.sentinelpay.merchant.entity.Merchant;
import com.sentinelpay.merchant.entity.MerchantStatus;
import com.sentinelpay.merchant.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PlatformUserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MerchantRepository merchantRepository;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.email())) {
            throw new DuplicateResourceException("User with this email already exists.");
        }

        PlatformUser user = new PlatformUser();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setRole(request.role());

        if (request.role() == UserRole.MERCHANT_ADMIN) {
            if (request.businessName() != null && !request.businessName().isEmpty()) {
                Merchant newMerchant = new Merchant();
                newMerchant.setBusinessName(request.businessName());
                newMerchant.setBusinessEmail(request.email());
                newMerchant.setBusinessPhone(request.businessPhone() != null ? request.businessPhone() : "0000000000");
                newMerchant.setStatus(MerchantStatus.PENDING);
                newMerchant.setKycStatus(KycStatus.PENDING);

                Merchant savedMerchant = merchantRepository.save(newMerchant);
                user.setMerchant(savedMerchant);
            }
            else if (request.merchantId() != null) {
                Merchant merchant = merchantRepository.findByIdAndDeletedAtIsNull(UUID.fromString(request.merchantId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Merchant", request.merchantId()));
                user.setMerchant(merchant);
            }
            else {
                throw new IllegalArgumentException("Either Business Details or Merchant ID is required.");
            }
        } else if (request.role() != UserRole.SUPER_ADMIN) {
            if (request.merchantId() == null) {
                throw new IllegalArgumentException("Merchant ID is required for this role.");
            }
            Merchant merchant = merchantRepository.findByIdAndDeletedAtIsNull(UUID.fromString(request.merchantId()))
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", request.merchantId()));
            user.setMerchant(merchant);
        }

        PlatformUser savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);

        return new AuthResponse(jwtToken, savedUser.getEmail(), savedUser.getRole().name());
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        PlatformUser user = userRepository.findByEmailAndDeletedAtIsNull(request.email())
            .orElseThrow();

        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken, user.getEmail(), user.getRole().name());
    }
}
