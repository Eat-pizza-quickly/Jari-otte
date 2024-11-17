package com.eatpizzaquickly.reservationservice.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class TossPaymentConfig {
    @Value("${payment.toss.test_client_api_key}")
    private String testClientApiKey;

    @Value("${payment.toss.test_secret_api_key}")
    private String testClientSecretKey;

    @Value("${payment.toss.success_url}")
    private String successUrl;

    @Value("${payment.toss.fail_url}")
    private String failureUrl;


    public static final String URL = "https://api.tosspayments.com/v1/payments/";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
