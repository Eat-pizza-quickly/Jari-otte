package com.eatpizzaquickly.jariotte.domain.reservation.repository;

import com.eatpizzaquickly.jariotte.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
