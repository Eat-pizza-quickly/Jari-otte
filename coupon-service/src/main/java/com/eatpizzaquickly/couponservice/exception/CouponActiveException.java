package com.eatpizzaquickly.couponservice.exception;


import com.eatpizzaquickly.couponservice.common.exception.BadRequestException;

public class CouponActiveException extends BadRequestException {
    public CouponActiveException(String message) {
        super(message);
    }
}
