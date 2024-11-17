package com.eatpizzaquickly.notificationservice.exception;

import jakarta.ws.rs.NotFoundException;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super("Invalid email: " + email);
    }
    public InvalidEmailException(String email, Throwable cause) {
        super("Invalid email: " + email, cause);
    }
}
