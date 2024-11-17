package com.eatpizzaquickly.reservationservice.reservation.repository;

import com.eatpizzaquickly.reservationservice.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface    ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByConcertId(Long concertId);
}
