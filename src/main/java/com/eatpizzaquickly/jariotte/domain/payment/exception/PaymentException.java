package com.eatpizzaquickly.jariotte.domain.payment.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
