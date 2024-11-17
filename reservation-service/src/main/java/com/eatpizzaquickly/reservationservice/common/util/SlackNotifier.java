package com.eatpizzaquickly.reservationservice.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class SlackNotifier {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${webhooks.url}")
    private String webhooksUrl;

    public void sendNotification(String message) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("text", message);

        try {
            restTemplate.postForEntity(webhooksUrl, payload, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
