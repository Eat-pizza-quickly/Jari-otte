package com.eatpizzaquickly.concertservice.client.reservation;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostReservationResponse {
    private  Long id;
    private  String status;
    private  LocalDateTime created_at;
}
