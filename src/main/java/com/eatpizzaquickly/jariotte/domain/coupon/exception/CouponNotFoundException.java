package com.eatpizzaquickly.jariotte.domain.coupon.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class CouponNotFoundException extends BadRequestException {
    public CouponNotFoundException(String message) {
        super(message);
    }
}
