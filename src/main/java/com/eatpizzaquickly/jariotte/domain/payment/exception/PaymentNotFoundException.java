package com.eatpizzaquickly.jariotte.domain.payment.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class PaymentNotFoundException extends BadRequestException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
