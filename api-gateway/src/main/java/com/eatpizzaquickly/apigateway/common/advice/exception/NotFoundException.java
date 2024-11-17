package com.eatpizzaquickly.apigateway.common.advice.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}