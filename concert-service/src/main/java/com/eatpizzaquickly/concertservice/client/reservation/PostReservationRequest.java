package com.eatpizzaquickly.concertservice.client.reservation;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostReservationRequest {
    private int price;
    private Long concertId;
    private Long seatId;
    private Long userId;
}
