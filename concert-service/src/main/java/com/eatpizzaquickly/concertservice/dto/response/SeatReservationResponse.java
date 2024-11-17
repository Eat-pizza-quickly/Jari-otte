package com.eatpizzaquickly.concertservice.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SeatReservationResponse {
    private Long id;
    private Long seatId;
    private String status;
    private LocalDateTime created_at;
    private Long userId;
}
