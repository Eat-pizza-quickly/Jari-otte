package com.eatpizzaquickly.couponservice.client;

import com.eatpizzaquickly.couponservice.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/v1/users/{userId}")
    ApiResponse<UserResponseDto> getUserById(@PathVariable("userId") Long userId);


    // 모든 사용자 정보를 가져오는 메서드
    @GetMapping("/api/v1/users")
    List<UserResponseDto> getAllUsers(); // 전체 사용자 정보 반환


}