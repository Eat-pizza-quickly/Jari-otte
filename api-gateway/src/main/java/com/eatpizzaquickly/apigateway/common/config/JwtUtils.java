package com.eatpizzaquickly.apigateway.common.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.util.Base64;

@Component
@Slf4j
public class JwtUtils {

    private final SecretKey key;

    public JwtUtils(@Value("${jwt.secret.key}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);  // Base64 디코딩 추가
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("JWT Key initialized"); // 키 초기화 확인용 로그
    }

    public String substringToken(String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return null;
        }
        return tokenHeader.substring(7);
    }

    public Claims extractClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("Token claims extracted successfully: {}", claims);  // 성공 로그
            return claims;
        } catch (Exception e) {
            log.error("Failed to extract claims from token: {}", e.getMessage());  // 실패 로그
            return null;
        }
    }
}