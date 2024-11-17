package com.eatpizzaquickly.couponservice.exception;

import com.eatpizzaquickly.couponservice.common.exception.BadRequestException;

public class CouponOutOfStockException extends BadRequestException {
    public CouponOutOfStockException(String message) {
        super(message);
    }
}
