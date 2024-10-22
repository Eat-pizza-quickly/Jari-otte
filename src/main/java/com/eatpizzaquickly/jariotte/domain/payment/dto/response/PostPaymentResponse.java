package com.eatpizzaquickly.jariotte.domain.payment.dto.response;

import com.eatpizzaquickly.jariotte.domain.payment.entity.PayMethod;
import com.eatpizzaquickly.jariotte.domain.payment.entity.PayStatus;
import com.eatpizzaquickly.jariotte.domain.payment.entity.Payment;
import lombok.Getter;

@Getter
public class PostPaymentResponse {
    private final String payUid;
    private final int amount;
    private final String payInfo;
    private final PayMethod payMethod;
    private final PayStatus payStatus;
    private final String successUrl;
    private final String failureUrl;

    public PostPaymentResponse(Payment payment, String successUrl, String failureUrl) {
        this.payUid = payment.getPay_uid();
        this.amount = payment.getAmount();
        this.payInfo = payment.getPay_info();
        this.payMethod = payment.getPay_method();
        this.payStatus = payment.getPay_status();
        this.successUrl = successUrl;
        this.failureUrl = failureUrl;
    }
}
