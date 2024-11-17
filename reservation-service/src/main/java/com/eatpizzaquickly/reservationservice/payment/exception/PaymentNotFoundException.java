package com.eatpizzaquickly.reservationservice.payment.exception;


import com.eatpizzaquickly.reservationservice.common.exception.BadRequestException;

public class PaymentNotFoundException extends BadRequestException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
