package com.eatpizzaquickly.reservationservice.reservation.dto;

import com.eatpizzaquickly.reservationservice.reservation.entity.Reservation;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostReservationResponse {
    private final Long id;
    private final String status;
    private final LocalDateTime created_at;

    public PostReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.status = reservation.getStatus().name();
        this.created_at = reservation.getCreatedAt();
    }

    public static PostReservationResponse from(Reservation reservation) {
        return new PostReservationResponse(
                reservation
        );
    }
}