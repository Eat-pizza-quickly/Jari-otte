package com.eatpizzaquickly.notificationservice.exception;

import jakarta.mail.MessagingException;
import jakarta.ws.rs.BadRequestException;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message, MessagingException e) {
        super(message);
    }
}
