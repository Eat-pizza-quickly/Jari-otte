package com.eatpizzaquickly.reservationservice.payment.exception;


import com.eatpizzaquickly.reservationservice.common.exception.BadRequestException;

public class PaymentProcessingException extends BadRequestException {
    public PaymentProcessingException(String message) {
        super(message);
    }
}
