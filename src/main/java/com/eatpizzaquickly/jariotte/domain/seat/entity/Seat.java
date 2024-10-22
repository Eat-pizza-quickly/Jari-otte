package com.eatpizzaquickly.jariotte.domain.seat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;
    @Column(nullable = false)
    private String seatLocation;
    @Column(nullable = false)
    private LocalDateTime reservedAt;
    private boolean isReserved = false;

}
