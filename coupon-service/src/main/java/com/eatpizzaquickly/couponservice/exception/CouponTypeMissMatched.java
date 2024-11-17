package com.eatpizzaquickly.couponservice.exception;

import com.eatpizzaquickly.couponservice.common.exception.BadRequestException;

public class CouponTypeMissMatched extends BadRequestException {
    public CouponTypeMissMatched(String message) {
        super(message);
    }
}
