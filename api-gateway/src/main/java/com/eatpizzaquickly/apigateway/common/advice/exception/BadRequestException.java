package com.eatpizzaquickly.apigateway.common.advice.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {super(message);}
}