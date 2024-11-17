package com.eatpizzaquickly.couponservice.exception;

import com.eatpizzaquickly.couponservice.common.exception.BadRequestException;
public class CouponNotFoundException extends BadRequestException {
    public CouponNotFoundException(String message) {
        super(message);
    }
}
