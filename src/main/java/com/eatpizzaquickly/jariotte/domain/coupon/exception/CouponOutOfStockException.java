package com.eatpizzaquickly.jariotte.domain.coupon.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class CouponOutOfStockException extends BadRequestException {
    public CouponOutOfStockException(String message) {
        super(message);
    }
}
