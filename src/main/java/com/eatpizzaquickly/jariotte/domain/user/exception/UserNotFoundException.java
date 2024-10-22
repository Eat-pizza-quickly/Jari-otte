package com.eatpizzaquickly.jariotte.domain.user.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
