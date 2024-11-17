package com.eatpizzaquickly.reservationservice.payment.exception;


import com.eatpizzaquickly.reservationservice.common.exception.BadRequestException;

public class PaymentSessionExpiredException extends BadRequestException {
    public PaymentSessionExpiredException(String message) {
        super(message);
    }
}
