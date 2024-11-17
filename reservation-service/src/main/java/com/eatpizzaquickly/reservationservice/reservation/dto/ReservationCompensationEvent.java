package com.eatpizzaquickly.reservationservice.reservation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReservationCompensationEvent {
    private Long concertId;
    private Long userId;
    private Long seatId;
    private Integer price;

    @Override
    public String toString() {
        return String.format(
                "ReservationCompensationEvent{concertId=%d, userId=%d, seatId=%d, price=%d}",
                concertId, userId, seatId, price
        );
    }
}
