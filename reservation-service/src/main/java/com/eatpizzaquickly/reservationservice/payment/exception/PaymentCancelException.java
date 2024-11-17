package com.eatpizzaquickly.reservationservice.payment.exception;


import com.eatpizzaquickly.reservationservice.common.exception.BadRequestException;

public class PaymentCancelException extends BadRequestException {
    public PaymentCancelException(String message) {
        super(message);
    }
}
