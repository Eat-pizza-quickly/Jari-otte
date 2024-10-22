package com.eatpizzaquickly.jariotte.domain.coupon.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class DuplicateCouponException extends BadRequestException {
    public DuplicateCouponException(String message) {
        super(message);
    }
}
