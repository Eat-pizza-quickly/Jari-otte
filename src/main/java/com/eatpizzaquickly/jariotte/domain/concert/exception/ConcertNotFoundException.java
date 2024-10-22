package com.eatpizzaquickly.jariotte.domain.concert.exception;

import com.eatpizzaquickly.jariotte.domain.common.exception.NotFoundException;

public class ConcertNotFoundException extends NotFoundException {
    private static final String MESSAGE = "해당 콘서트가 존재하지 않습니다.";

    public ConcertNotFoundException() {super(MESSAGE);}

    public ConcertNotFoundException(String message) {super(message);}
}
