package com.eatpizzaquickly.concertservice.exception.detail;

public class RedisException extends RuntimeException{
    private static final String MESSAGE = "Redis 에 예상치 못한 문제가 발생했습니다.";

    public RedisException() {super(MESSAGE);}

    public RedisException(String message) {super(message);}
}
