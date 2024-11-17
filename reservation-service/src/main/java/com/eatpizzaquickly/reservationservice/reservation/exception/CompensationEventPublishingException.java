package com.eatpizzaquickly.reservationservice.reservation.exception;

public class CompensationEventPublishingException extends RuntimeException{
    private static final String MESSAGE = "보상 트랜잭션 이벤트 발행중에 예외가 발생했습니다.";

    public CompensationEventPublishingException() {super(MESSAGE);}

    public CompensationEventPublishingException(String message) {super(message);}
}
