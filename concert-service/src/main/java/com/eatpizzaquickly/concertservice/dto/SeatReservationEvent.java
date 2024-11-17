package com.eatpizzaquickly.concertservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SeatReservationEvent {
    private Long concertId;
    private Long userId;
    private Long seatId;
    private Integer price;

    @Builder
    private SeatReservationEvent(Long concertId, Long userId, Long seatId, Integer price) {
        this.concertId = concertId;
        this.userId = userId;
        this.seatId = seatId;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format(
                "SeatReservationEvent{concertId=%d, userId=%d, seatId=%d, price=%d}",
                concertId, userId, seatId, price
        );
    }
}
