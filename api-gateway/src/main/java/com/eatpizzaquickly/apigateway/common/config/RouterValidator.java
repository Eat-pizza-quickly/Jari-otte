package com.eatpizzaquickly.apigateway.common.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {
    public static final List<String> openEndPoints = List.of(
            "/api/v1/users/signin", "/api/v1/users/signup", "/actuator/prometheus"
    );

    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> openEndPoints.stream()
                    .noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}