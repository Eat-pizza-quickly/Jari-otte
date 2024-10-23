package com.eatpizzaquickly.jariotte.domain.reservation.dto.request;

import lombok.Getter;

@Getter
public class PostReservationRequest {
    private int price;
    private Long concertId;
    private Long seatId;
}
