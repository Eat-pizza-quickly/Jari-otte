package com.eatpizzaquickly.jariotte.domain.reservation.entity;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import com.eatpizzaquickly.jariotte.domain.seat.entity.Seat;
import com.eatpizzaquickly.jariotte.domain.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Builder
    public Reservation(int price, ReservationStatus reservationStatus, Concert concert) {
        this.price = price;
        this.status = reservationStatus;
        this.concert = concert;
        this.createdAt = LocalDateTime.now();
    }

    public void setSeatToReserved(Seat seat) {
        seat.changeReserved(true);
    }

    public void setSeatToCancelled(Seat seat) {
        seat.changeReserved(false);
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
