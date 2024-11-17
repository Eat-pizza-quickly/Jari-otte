package com.eatpizzaquickly.userservice.exception;


import com.eatpizzaquickly.userservice.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
