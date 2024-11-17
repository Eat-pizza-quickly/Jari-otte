package com.eatpizzaquickly.userservice.exception;


import com.eatpizzaquickly.userservice.common.exception.BadRequestException;

public class DuplicateUserException extends BadRequestException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
