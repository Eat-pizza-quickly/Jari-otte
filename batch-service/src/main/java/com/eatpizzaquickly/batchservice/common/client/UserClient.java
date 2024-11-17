package com.eatpizzaquickly.batchservice.common.client;


import com.eatpizzaquickly.batchservice.common.config.FeignConfig;
import com.eatpizzaquickly.batchservice.settlement.dto.request.HostPointRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service",configuration = FeignConfig.class)
public interface UserClient {
    @PostMapping("/api/v1/users/addpoints")
    ResponseEntity<String> addPointsToHost(@RequestBody List<HostPointRequestDto> hostpoints);
}