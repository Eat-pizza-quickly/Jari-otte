package com.eatpizzaquickly.jariotte.domain.common.config;


import com.eatpizzaquickly.jariotte.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (JWT 사용 시 필요하지 않음)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않음 (JWT 기반 인증)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/jari-otte/users","/api/v1/jari-otte/users/login").permitAll() // 인증 관련 엔드포인트는 모두 접근 가능
                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .build();
    }

}
