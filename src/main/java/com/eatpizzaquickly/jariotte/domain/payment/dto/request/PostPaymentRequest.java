package com.eatpizzaquickly.jariotte.domain.payment.dto.request;

import lombok.Getter;

@Getter
public class PostPaymentRequest {
    private Long reservationId;
    private Long amount;
    private String payInfo;
}
