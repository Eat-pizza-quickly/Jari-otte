package com.eatpizzaquickly.jariotte.domain.payment.dto.response;

import com.eatpizzaquickly.jariotte.domain.payment.entity.PayMethod;
import com.eatpizzaquickly.jariotte.domain.payment.entity.PayStatus;
import com.eatpizzaquickly.jariotte.domain.payment.entity.Payment;
import lombok.Getter;

@Getter
public class PostPaymentResponse {
    private final String payUid;
    private final Long amount;
    private final String payInfo;
    private final PayMethod payMethod;
    private final PayStatus payStatus;
    private final String successUrl;
    private final String failureUrl;

    public PostPaymentResponse(Payment payment, String successUrl, String failureUrl) {
        this.payUid = payment.getPayUid();
        this.amount = payment.getAmount();
        this.payInfo = payment.getPayInfo();
        this.payMethod = payment.getPayMethod();
        this.payStatus = payment.getPayStatus();
        this.successUrl = successUrl;
        this.failureUrl = failureUrl;
    }
}
