package com.eatpizzaquickly.reservationservice.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    public RequestInterceptor requestLoggingInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 요청 경로와 HTTP 메서드 로깅
                log.info("Feign Request to URI: {} Method: {}", template.url(), template.method());

                // 요청 바디가 있다면 바디 로깅
                if (template.body() != null) {
                    log.info("Request Body: {}", new String(template.body()));
                }
            }
        };
    }
}