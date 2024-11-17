package com.eatpizzaquickly.reservationservice.payment.dto.response;

import com.eatpizzaquickly.reservationservice.payment.entity.PayMethod;
import com.eatpizzaquickly.reservationservice.payment.entity.PayStatus;
import com.eatpizzaquickly.reservationservice.payment.entity.Payment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetPaymentResponse {
    private String payUid;
    private String paymentKey;
    private Long amount;
    private String payInfo;
    private PayMethod payMethod;
    private PayStatus payStatus;
    private String message;
    private String code;

    public GetPaymentResponse(Payment payment) {
        this.payUid = payment.getPayUid();
        this.amount = payment.getAmount();
        this.payInfo = payment.getPayInfo();
        this.payMethod = payment.getPayMethod();
        this.payStatus = payment.getPayStatus();
    }

    @Builder
    public GetPaymentResponse(String paymentKey, Long amount, String payInfo, PayMethod payMethod, PayStatus payStatus, String message, String code) {
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.payInfo = payInfo;
        this.payMethod = payMethod;
        this.payStatus = payStatus;
        this.message = message;
        this.code = code;
    }
}