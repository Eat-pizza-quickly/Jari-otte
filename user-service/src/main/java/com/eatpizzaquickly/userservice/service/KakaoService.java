package com.eatpizzaquickly.userservice.service;

import com.eatpizzaquickly.userservice.dto.KakaoLoginResponseDto;
import com.eatpizzaquickly.userservice.dto.KakaoUserDto;
import com.eatpizzaquickly.userservice.exception.KakaoRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoService {

    private final String clientId;
    private final String redirectUri;
    private final RestTemplate restTemplate;

    public KakaoService(
            @Value("${kakao.client_id}") String clientId,
            @Value("${kakao.redirect_uri}") String redirectUri,
            RestTemplateBuilder builder) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.restTemplate = builder.build();
    }

    public String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<KakaoLoginResponseDto> response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    request,
                    KakaoLoginResponseDto.class
            );
            log.info("Kakao API response status: {}", response.getStatusCode());
            log.debug("Kakao API response body: {}", response.getBody());
            return response.getBody().getAccessToken();
        } catch (Exception e) {
            log.error("Kakao API request error: {}", e.getMessage());
            throw new KakaoRequestException("카카오 API 요청 중 오류가 발생했습니다.");
        }
    }

    public KakaoUserDto getKakaoUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity<>(headers);
        try {
            ResponseEntity<KakaoUserDto> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    request,
                    KakaoUserDto.class);
            log.info("Kakao API response status: {}", response.getStatusCode());
            log.debug("Kakao API response body: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.error("Kakao API request error: {}", e.getMessage());
            throw new KakaoRequestException("카카오 API 요청 중 오류가 발생했습니다.");
        }
    }
}
