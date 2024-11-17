package com.eatpizzaquickly.couponservice.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@RequiredArgsConstructor
public class FeignClientConfig {
    private final ObjectMapper objectMapper;
    @Bean
    public RequestInterceptor userIdRequestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String userId = attributes.getRequest().getHeader("X-Authenticated-User");
                if (userId != null) {
                    // Feign 요청 헤더에 userId 추가
                    requestTemplate.header("X-Authenticated-User", userId);
                }
            }
        };
    }


    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(() -> new HttpMessageConverters(
                new MappingJackson2HttpMessageConverter(objectMapper)
        )));
    }
}
