package com.sentinelpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * SentinelPay - Payment Infrastructure Platform
 *
 * Entry point of the Spring Boot app.
 *
 * EnableJpaAuditing - isse @CreatedDate, @LastModifiedDate automatically
 * populate ho jaate hain entities mein. Meri pehli mistake thi ki main
 * manually setter call kar raha tha har entity mein 😅
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class SentinelPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelPayApplication.class, args);
    }
}
