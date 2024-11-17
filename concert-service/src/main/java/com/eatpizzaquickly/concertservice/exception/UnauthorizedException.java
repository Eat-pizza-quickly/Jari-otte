package com.eatpizzaquickly.concertservice.exception;

public class UnauthorizedException extends RuntimeException{
    private static final String MESSAGE = "권한이 없습니다.";

    public UnauthorizedException() {super(MESSAGE);}

    public UnauthorizedException(Throwable cause) {super(cause);}
}
