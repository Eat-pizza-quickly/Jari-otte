package com.eatpizzaquickly.concertservice.exception.detail;

import com.eatpizzaquickly.concertservice.exception.ForbiddenException;

public class InvalidReservationFlowException extends ForbiddenException {
    private static final String MESSAGE = "정상적인 흐름이 아닙니다.";

    public InvalidReservationFlowException() {super(MESSAGE);}

    public InvalidReservationFlowException(String message) {super(message);}
}
