package com.eatpizzaquickly.jariotte.domain.user.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.NotFoundException;

public class PasswordNotMatchException extends NotFoundException {
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
