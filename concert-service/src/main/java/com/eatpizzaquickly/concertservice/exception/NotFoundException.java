package com.eatpizzaquickly.concertservice.exception;

public class NotFoundException extends RuntimeException{
    private static final String MESSAGE = "해당 자원을 찾을 수 없습니다.";

    public NotFoundException() {super(MESSAGE);}

    public NotFoundException(String message) {super(message);}
}
