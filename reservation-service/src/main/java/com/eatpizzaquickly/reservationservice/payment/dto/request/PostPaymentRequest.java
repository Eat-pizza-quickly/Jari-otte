package com.eatpizzaquickly.reservationservice.payment.dto.request;

import lombok.Getter;

@Getter
public class PostPaymentRequest {
    private Long reservationId;
    private Long amount;
    private String payInfo;
}
