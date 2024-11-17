package com.eatpizzaquickly.concertservice.client;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ReservationCompensationEvent {
    private Long concertId;
    private Long userId;
    private Long seatId;
    private Integer price;

    @Builder
    private ReservationCompensationEvent(Long concertId, Long userId, Long seatId, Integer price) {
        this.concertId = concertId;
        this.userId = userId;
        this.seatId = seatId;
        this.price = price;
    }
}
