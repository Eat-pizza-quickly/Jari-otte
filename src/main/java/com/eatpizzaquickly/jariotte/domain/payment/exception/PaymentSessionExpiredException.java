package com.eatpizzaquickly.jariotte.domain.payment.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class PaymentSessionExpiredException extends BadRequestException {
    public PaymentSessionExpiredException(String message) {
        super(message);
    }
}
