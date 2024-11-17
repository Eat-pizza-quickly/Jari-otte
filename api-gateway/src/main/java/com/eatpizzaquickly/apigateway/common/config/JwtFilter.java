package com.eatpizzaquickly.apigateway.common.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter implements WebFilter {
    private final JwtUtils jwtUtil;
    private final RouterValidator routerValidator;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.info("현재 요청 경로: {}", path);

        // 인증 예외 경로 설정
        if (path.equals("/api/v1/users") || path.equals("/api/v1/users/login") ||
                path.equals("/actuator/health") || path.equals("/actuator/prometheus")
                || path.startsWith("/api/v1/users/sendmail") || path.startsWith("/api/v1/concert/search")
                || path.equals("api/v1/api/v1/concerts/search/autocomplete")) {
            return chain.filter(exchange);  // 경로에 대한 필터링 없이 통과
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
        }

        try {
            String token = jwtUtil.substringToken(authHeader);
            if (redisTemplate.hasKey("BL:" + token)) {
                return onError(exchange, "Expired JWT", HttpStatus.BAD_REQUEST);
            }
            Claims claims = jwtUtil.extractClaims(token);

            if (claims == null) {
                return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }

            Long userId = claims.get("userId", Long.class);
            String userRole = claims.get("userRole", String.class);
            log.info("user ID: {}, Role: {}", userId, userRole);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    CustomUserDetails.builder()
                            .userId(userId)
                            .role(userRole)
                            .build(),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(userRole))
            );

            return chain.filter(
                    exchange.mutate()
                            .request(request.mutate()
                                    .header("X-Authenticated-User", String.valueOf(userId))
                                    .build())
                            .build()
            ).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

        } catch (Exception e) {
            log.error("JWT validation failed", e);
            return onError(exchange, "JWT validation failed", HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error("Error in JwtFilter: {}", err);
        return response.setComplete();
    }
}