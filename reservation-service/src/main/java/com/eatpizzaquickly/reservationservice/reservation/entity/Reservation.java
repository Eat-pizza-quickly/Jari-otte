package com.eatpizzaquickly.reservationservice.reservation.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Long userId;

    private Long seatId;

    private Long concertId;

    @Builder
    public Reservation(int price, Long userId, Long seatId, ReservationStatus reservationStatus, Long concertId) {
        this.price = price;
        this.userId = userId;
        this.seatId = seatId;
        this.status = reservationStatus;
        this.concertId = concertId;
        this.createdAt = LocalDateTime.now();
    }

    public void statusUpdate(ReservationStatus reservationStatus) {
        this.status = reservationStatus;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
