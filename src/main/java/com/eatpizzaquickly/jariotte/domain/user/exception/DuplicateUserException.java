package com.eatpizzaquickly.jariotte.domain.user.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class DuplicateUserException extends BadRequestException {
    public DuplicateUserException(String message) {
        super(message);
    }
}