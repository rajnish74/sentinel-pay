package com.sentinelpay.merchant;

import com.sentinelpay.merchant.entity.KycStatus;
import com.sentinelpay.merchant.entity.Merchant;
import com.sentinelpay.merchant.entity.MerchantStatus;
import com.sentinelpay.merchant.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Full-stack integration test - real PostgreSQL in Docker via Testcontainers.
 *
 * Why Testcontainers instead of H2?
 * - H2 is a "fake" DB - syntax differences, missing features
 * - A test passing on H2 doesn't guarantee it passes on PostgreSQL
 * - Testcontainers spins up real postgres:16 image - same as prod
 * - Slower but trustworthy
 *
 * Interview answer: "I use Testcontainers for integration tests because
 * H2 lies - it doesn't support UUID generation the same way, JSONB, window
 * functions, or PL/pgSQL. For a payment platform, if my test passes on H2
 * but fails on prod PostgreSQL, the test is worthless."
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class MerchantApiIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("sentinelpay_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MerchantRepository merchantRepository;

    private Merchant savedMerchant;

    @BeforeEach
    void setUp() {
        merchantRepository.deleteAll();

        Merchant merchant = new Merchant();
        merchant.setBusinessName("Test Merchant");
        merchant.setBusinessEmail("test@example.com");
        merchant.setBusinessPhone("+919999999999");
        merchant.setStatus(MerchantStatus.ACTIVE);
        merchant.setKycStatus(KycStatus.VERIFIED);

        savedMerchant = merchantRepository.save(merchant);
    }

    @Test
    void getMerchant_byValidId_returns200WithMerchantData() throws Exception {
        mockMvc.perform(get("/api/v1/merchants/{id}", savedMerchant.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(savedMerchant.getId().toString()))
            .andExpect(jsonPath("$.businessName").value("Test Merchant"))
            .andExpect(jsonPath("$.businessEmail").value("test@example.com"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.kycStatus").value("VERIFIED"));
    }

    @Test
    void getMerchant_byNonExistentId_returns404() throws Exception {
        String randomId = "00000000-0000-0000-0000-000000000000";

        mockMvc.perform(get("/api/v1/merchants/{id}", randomId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void getMerchant_bySoftDeletedId_returns404() throws Exception {
        // Soft delete the merchant
        savedMerchant.setDeletedAt(java.time.Instant.now());
        merchantRepository.save(savedMerchant);

        mockMvc.perform(get("/api/v1/merchants/{id}", savedMerchant.getId()))
            .andExpect(status().isNotFound());
    }


    @Test
    void createMerchant_withValidData_returns201() throws Exception {
        String requestBody = """
            {
              "businessName": "New Merchant",
              "businessEmail": "new@example.com",
              "businessPhone": "+919999999999",
              "panNumber": "ABCDE1234F"
            }
            """;

        mockMvc.perform(post("/api/v1/merchants")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.businessEmail").value("new@example.com"))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createMerchant_withInvalidData_returns400() throws Exception {
        String requestBody = """
            {
              "businessName": "",
              "businessEmail": "invalid-email",
              "businessPhone": "123"
            }
            """;

        mockMvc.perform(post("/api/v1/merchants")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
            .andExpect(jsonPath("$.fieldErrors.businessEmail").exists());
    }
}
