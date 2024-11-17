package com.eatpizzaquickly.batchservice.common.client;

import com.eatpizzaquickly.batchservice.settlement.dto.request.PaymentRequestDto;
import com.eatpizzaquickly.batchservice.settlement.dto.response.PaymentResponseDto;
import com.eatpizzaquickly.batchservice.settlement.entity.PayStatus;
import com.eatpizzaquickly.batchservice.settlement.entity.SettlementStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "reservation-service")
public interface PaymentClient {
    @GetMapping("/api/v1/payments")
    List<PaymentResponseDto> getPaymentsByStatusAfterId(
            @RequestParam(name = "settlementStatus") SettlementStatus settlementStatus,
            @RequestParam(name = "payStatus") PayStatus payStatus,
            @RequestParam(name = "size") int chunk,
            @RequestParam(name = "offset") int currentOffset);

    @PutMapping("/api/v1/payments")
    ResponseEntity<String> updatePayments(@RequestBody List<PaymentRequestDto> payments);
}
