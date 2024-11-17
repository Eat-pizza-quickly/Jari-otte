package com.eatpizzaquickly.notificationservice.exception;

import jakarta.ws.rs.BadRequestException;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
