package com.eatpizzaquickly.concertservice.exception.detail;

public class CompensateReservationFailureException extends RuntimeException{
    private static final String MESSAGE = "보상작업중 예외가 발생했습니다.";

    public CompensateReservationFailureException() {super(MESSAGE);}

    public CompensateReservationFailureException(String message) {super(message);}

}
