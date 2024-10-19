package com.eatpizzaquickly.jariotte.user.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.NotFoundException;

public class NotMatchException extends NotFoundException {
    public NotMatchException(String message) {
        super(message);
    }
}
