package com.eatpizzaquickly.jariotte.domain.concert.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.NotFoundException;

public class VenueNotFountException extends NotFoundException {
    private static final String MESSAGE = "해당 공연장을 찾을 수 없습니다.";

    public VenueNotFountException() {super(MESSAGE);}

    public VenueNotFountException(String message) {super(message);}
}
