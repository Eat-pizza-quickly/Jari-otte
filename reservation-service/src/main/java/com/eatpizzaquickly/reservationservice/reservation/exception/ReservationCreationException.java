package com.eatpizzaquickly.reservationservice.reservation.exception;

public class ReservationCreationException extends RuntimeException {
    private static final String MESSAGE = "이벤트를 소비하여 DB에 예매정보를 저장하는 중에 예외가 발생했습니다.";

    public ReservationCreationException() {super(MESSAGE);}

    public ReservationCreationException(String message) {super(message);}
}
