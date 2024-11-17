package com.eatpizzaquickly.concertservice.exception;

public class ForbiddenException extends RuntimeException{
    private static final String MESSAGE = "금지된 요청입니다.";

    public ForbiddenException() {super(MESSAGE);}

    public ForbiddenException(String message) {super(message);}
}
