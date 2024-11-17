package com.eatpizzaquickly.concertservice.exception.detail;

import com.eatpizzaquickly.concertservice.exception.BadRequestException;

public class SeatAlreadyReservedException extends BadRequestException {
    private static final String MESSAGE = "이미 예약된 좌석입니다.";

    public SeatAlreadyReservedException() {super(MESSAGE);}

    public SeatAlreadyReservedException(String message) {super(message);}
}
