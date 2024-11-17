package com.eatpizzaquickly.reservationservice.payment.repository;

import com.eatpizzaquickly.reservationservice.payment.dto.response.PaymentResponseDto;
import com.eatpizzaquickly.reservationservice.payment.entity.PayStatus;
import com.eatpizzaquickly.reservationservice.payment.entity.Payment;
import com.eatpizzaquickly.reservationservice.payment.entity.SettlementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PaymentQueryDslRepository {
    Page<PaymentResponseDto> getPaymentsByStatus(SettlementStatus settlementStatus, PayStatus payStatus, LocalDateTime sevenDaysAgo, int currentOffset, int chunk);
}
