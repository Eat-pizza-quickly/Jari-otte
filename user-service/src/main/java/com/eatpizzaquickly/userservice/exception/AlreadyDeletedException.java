package com.eatpizzaquickly.userservice.exception;


import com.eatpizzaquickly.userservice.common.exception.BadRequestException;

public class AlreadyDeletedException extends BadRequestException {
    public AlreadyDeletedException(String message) {
        super(message);
    }
}
