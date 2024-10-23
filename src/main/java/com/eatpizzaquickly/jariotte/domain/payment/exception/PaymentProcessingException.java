package com.eatpizzaquickly.jariotte.domain.payment.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class PaymentProcessingException extends BadRequestException{
    public PaymentProcessingException(String message) {
        super(message);
    }
}
