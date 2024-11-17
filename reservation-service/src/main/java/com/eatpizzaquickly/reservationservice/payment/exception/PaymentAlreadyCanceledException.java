package com.eatpizzaquickly.reservationservice.payment.exception;


import com.eatpizzaquickly.reservationservice.common.exception.BadRequestException;

public class PaymentAlreadyCanceledException extends BadRequestException {
    public PaymentAlreadyCanceledException(String message) {
        super(message);
    }
}
