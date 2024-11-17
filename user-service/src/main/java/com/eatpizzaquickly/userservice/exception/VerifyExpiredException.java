package com.eatpizzaquickly.userservice.exception;

import com.eatpizzaquickly.userservice.common.exception.NotFoundException;

public class VerifyExpiredException extends NotFoundException {
    public VerifyExpiredException(String message) {
        super(message);
    }
}
