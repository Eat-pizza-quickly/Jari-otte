package com.eatpizzaquickly.couponservice.exception;

import com.eatpizzaquickly.couponservice.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
