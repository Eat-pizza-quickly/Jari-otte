package com.eatpizzaquickly.reservationservice.review.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping(value = "/api/v1/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserResponseDto getUserById(@PathVariable Long userId);
}
