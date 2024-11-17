package com.eatpizzaquickly.userservice.exception;

import com.eatpizzaquickly.userservice.common.exception.NotFoundException;

public class PasswordNotMatchException extends NotFoundException {
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
