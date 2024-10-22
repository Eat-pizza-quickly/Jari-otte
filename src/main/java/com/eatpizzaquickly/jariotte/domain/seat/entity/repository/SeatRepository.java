package com.eatpizzaquickly.jariotte.domain.seat.entity.repository;

import com.eatpizzaquickly.jariotte.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
