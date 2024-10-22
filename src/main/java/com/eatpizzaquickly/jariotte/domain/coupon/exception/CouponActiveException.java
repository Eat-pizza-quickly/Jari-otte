package com.eatpizzaquickly.jariotte.domain.coupon.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class CouponActiveException extends BadRequestException {
    public CouponActiveException(String message) {
        super(message);
    }
}
