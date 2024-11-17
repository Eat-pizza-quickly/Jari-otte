package com.eatpizzaquickly.reservationservice.payment.dto.request;

import lombok.Getter;

@Getter
public class PaymentConfirmRequest {
    private final String paymentKey;
    private final String orderId;
    private final Long amount;

    public PaymentConfirmRequest(String paymentKey, String orderId, Long amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}
