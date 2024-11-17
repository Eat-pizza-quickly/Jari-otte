package com.eatpizzaquickly.userservice.exception;

import com.eatpizzaquickly.userservice.common.exception.BadRequestException;

public class UserNotVerifiedException extends BadRequestException {

    public UserNotVerifiedException(String message) {
        super(message);
    }
}
