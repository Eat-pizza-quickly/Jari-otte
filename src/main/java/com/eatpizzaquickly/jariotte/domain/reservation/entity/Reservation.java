package com.eatpizzaquickly.jariotte.domain.reservation.entity;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import com.eatpizzaquickly.jariotte.domain.reservation.dto.request.PostReservationRequest;
import com.eatpizzaquickly.jariotte.domain.seat.entity.Seat;
import com.eatpizzaquickly.jariotte.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    public Reservation(PostReservationRequest request, ReservationStatus reservationStatus, Concert concert, Seat seat) {
        this.price = request.getPrice();
        this.status = reservationStatus;
        this.concert = concert;
        this.seat = seat;
        this.createdAt = LocalDateTime.now();
    }
}
