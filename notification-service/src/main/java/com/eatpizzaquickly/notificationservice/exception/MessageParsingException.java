package com.eatpizzaquickly.notificationservice.exception;

import jakarta.ws.rs.BadRequestException;

public class MessageParsingException extends RuntimeException {
    public MessageParsingException(String message) {
        super(message);
    }
}
