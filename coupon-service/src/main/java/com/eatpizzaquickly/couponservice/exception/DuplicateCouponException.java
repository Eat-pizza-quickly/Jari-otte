package com.eatpizzaquickly.couponservice.exception;

import com.eatpizzaquickly.couponservice.common.exception.BadRequestException;

public class DuplicateCouponException extends BadRequestException {
    public DuplicateCouponException(String message) {
        super(message);
    }

    public DuplicateCouponException(String message, Throwable cause) {
        super(message);
    }

}
