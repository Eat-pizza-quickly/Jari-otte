package com.eatpizzaquickly.reservationservice.payment.client;

import com.eatpizzaquickly.reservationservice.common.config.FeignConfig;
import com.eatpizzaquickly.reservationservice.payment.dto.request.HostPointRequestDto;
import com.eatpizzaquickly.reservationservice.payment.dto.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service",configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/api/v1/users/{userId}")
    ApiResponse<UserResponseDto> getUserById(@PathVariable("userId") Long userId);

    @PostMapping("/api/v1/users/addpoints")
    ResponseEntity<String> addPointsToHost(@RequestBody List<HostPointRequestDto> hostpoints);
}