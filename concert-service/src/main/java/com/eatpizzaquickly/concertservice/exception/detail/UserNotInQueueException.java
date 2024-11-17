package com.eatpizzaquickly.concertservice.exception.detail;

import com.eatpizzaquickly.concertservice.exception.NotFoundException;

public class UserNotInQueueException extends NotFoundException {
    private static final String MESSAGE = "사용자가 해당 대기열에 존재하지 않습니다.";

    public UserNotInQueueException() {super(MESSAGE);}

    public UserNotInQueueException(String message) {super(message);}
}
