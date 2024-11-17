package com.eatpizzaquickly.concertservice.exception.detail;

public class ReservationEventPublishingException extends RuntimeException{
    private static final String MESSAGE = "좌석 예매 이벤트 발행 중 문제가 발생헀습니다.";

    public ReservationEventPublishingException() {super(MESSAGE);}

    public ReservationEventPublishingException(String message) {super(message);}
}
