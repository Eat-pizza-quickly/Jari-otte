package com.eatpizzaquickly.couponservice.exception;

import com.eatpizzaquickly.couponservice.common.exception.BadRequestException;

public class CouponLockException extends RuntimeException {
    public CouponLockException(String message) {
        super(message);
    }
    public CouponLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
