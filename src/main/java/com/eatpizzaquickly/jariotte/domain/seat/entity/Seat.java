package com.eatpizzaquickly.jariotte.domain.seat.entity;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    private Integer seatNumber;

    private boolean isReserved = false;

    @JoinColumn(name = "concert_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Concert concert;

    @Builder
    private Seat(Integer seatNumber, boolean isReserved, Concert concert) {
        this.seatNumber = seatNumber;
        this.isReserved = isReserved;
        this.concert = concert;
    }
}
