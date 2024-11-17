package com.eatpizzaquickly.concertservice.exception.detail;

public class RequestLimitExceededException extends RuntimeException{
    public RequestLimitExceededException(Long concertId) {
        super("/api/v1/concerts/" + concertId + "/queue/position");
    }

    public RequestLimitExceededException(String message) {super(message);}
}
