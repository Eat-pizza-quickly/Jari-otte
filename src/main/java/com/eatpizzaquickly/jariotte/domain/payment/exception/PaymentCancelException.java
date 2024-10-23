package com.eatpizzaquickly.jariotte.domain.payment.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class PaymentCancelException extends BadRequestException {
    public PaymentCancelException(String message) {
        super(message);
    }
}
