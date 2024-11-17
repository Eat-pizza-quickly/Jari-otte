package com.eatpizzaquickly.reservationservice.payment.dto.response;

import com.eatpizzaquickly.reservationservice.payment.entity.PayStatus;
import com.eatpizzaquickly.reservationservice.payment.entity.Payment;
import com.eatpizzaquickly.reservationservice.payment.entity.SettlementStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResponseDto {
    private Long id;
    private SettlementStatus settlementStatus;
    private PayStatus payStatus;
    private Long amount;
    private Long concertId;

    public static PaymentResponseDto from(Payment payment) {
        return new PaymentResponseDto(
                payment.getId(),
                payment.getSettlementStatus(),
                payment.getPayStatus(),
                payment.getAmount(),
                payment.getReservation().getConcertId()
        );
    }
}

