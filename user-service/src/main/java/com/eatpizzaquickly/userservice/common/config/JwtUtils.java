package com.eatpizzaquickly.userservice.common.config;

import com.eatpizzaquickly.userservice.common.exception.NotFoundException;
import com.eatpizzaquickly.userservice.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {


    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    private static final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 14일 (2주)


    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Long id, UserRole userRole) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(id.toString())
                .claim("userId", id)
                .claim("userRole", userRole)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String createRefreshToken(Long id, UserRole userRole) {
        Date date = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("userId", id)
                .claim("userRole", userRole)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String invalidToken(String token) {
        Date now = new Date();

        Claims claims = extractClaims(token);
        return Jwts.builder()
                .setSubject(claims.getSubject())
                .setClaims(claims)
                .setIssuedAt(now) // 발행 시간을 현재 시간으로 설정
                .setExpiration(now) // 만료 시간을 현재 시간으로 설정
                .signWith(key, signatureAlgorithm) // 기존과 동일한 키와 알고리즘 사용
                .compact();
    }

    public Long getRefreshTokenExpirationTime() {
        return REFRESH_TOKEN_TIME;
    }

    public String getUserIdFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }


    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length()).trim();
        }
        throw new NotFoundException("토큰을 찾을 수 없습니다");
    }

    public Claims extractClaims(String token) {
        try {
            // Bearer 제거 및 공백 제거
            if (token.startsWith(BEARER_PREFIX)) {
                token = token.substring(BEARER_PREFIX.length()).trim();
            }

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }


}