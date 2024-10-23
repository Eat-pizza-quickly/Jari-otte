package com.eatpizzaquickly.jariotte.domain.payment.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class PaymentAlreadyCanceledException extends BadRequestException {
    public PaymentAlreadyCanceledException(String message) {
        super(message);
    }
}
