package com.eatpizzaquickly.reservationservice.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReservationCreateRequest {
    private int price;
    private Long concertId;
    private Long seatId;
    private Long userId;

    public static ReservationCreateRequest from(SeatReservationEvent event) {
        return new ReservationCreateRequest(event.getPrice(), event.getConcertId(), event.getSeatId(), event.getUserId());
    }
}
