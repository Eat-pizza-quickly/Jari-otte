package com.eatpizzaquickly.apigateway.common.config;

import com.eatpizzaquickly.apigateway.common.advice.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.error("Not Authenticated Request", ex);
        log.error("Request Uri : {}", exchange.getRequest().getURI());

        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .message("인증 실패")
                .data(LocalDateTime.now())
                .build();
        try {
            byte[] errorByte = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .writeValueAsBytes(apiResponse);
            DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(errorByte);
            return serverHttpResponse.writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return serverHttpResponse.setComplete();
        }
    }
}