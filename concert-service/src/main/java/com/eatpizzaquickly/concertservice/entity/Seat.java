package com.eatpizzaquickly.concertservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Column(name = "seat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
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

    public void changeReserved(boolean status) {
        this.isReserved = status;
    }

}
