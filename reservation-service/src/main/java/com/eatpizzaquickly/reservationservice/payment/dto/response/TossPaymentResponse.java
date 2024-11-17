package com.eatpizzaquickly.reservationservice.payment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentResponse {
    private String paymentKey;
    private String orderId;
    private Long totalAmount;
    private String status;
    private String createdAt;
    private String lastTransactionKey;
    private String failReason;
    private  Long cancleAmount;
}