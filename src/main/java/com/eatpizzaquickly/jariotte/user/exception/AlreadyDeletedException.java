package com.eatpizzaquickly.jariotte.user.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

public class AlreadyDeletedException extends BadRequestException {
    public AlreadyDeletedException(String message) {
        super(message);
    }
}
