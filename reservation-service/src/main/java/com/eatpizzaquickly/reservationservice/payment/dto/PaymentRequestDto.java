package com.eatpizzaquickly.reservationservice.payment.dto;

import com.eatpizzaquickly.reservationservice.payment.entity.PayStatus;
import com.eatpizzaquickly.reservationservice.payment.entity.SettlementStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentRequestDto {
    private Long id;
    private SettlementStatus settlementStatus;
    private PayStatus payStatus;
    private Long amount;
}
