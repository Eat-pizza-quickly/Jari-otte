package com.eatpizzaquickly.userservice.exception;

import com.eatpizzaquickly.userservice.common.exception.BadRequestException;

public class TokenNotMatchException extends BadRequestException {

    public TokenNotMatchException(String message) {
        super(message);
    }
}
