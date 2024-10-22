package com.eatpizzaquickly.jariotte.domain.payment.dto.response;

import com.eatpizzaquickly.jariotte.domain.payment.entity.PayMethod;
import com.eatpizzaquickly.jariotte.domain.payment.entity.PayStatus;
import com.eatpizzaquickly.jariotte.domain.payment.entity.Payment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPaymentResponse {
    private final String payUid;
    private String paymentKey;
    private final int amount;
    private final String payInfo;
    private final PayMethod payMethod;
    private final PayStatus payStatus;
    private String message;
    private String code;

    public GetPaymentResponse(Payment payment) {
        this.payUid = payment.getPay_uid();
        this.amount = payment.getAmount();
        this.payInfo = payment.getPay_info();
        this.payMethod = payment.getPay_method();
        this.payStatus = payment.getPay_status();
    }
}
