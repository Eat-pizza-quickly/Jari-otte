package com.eatpizzaquickly.jariotte.domain.seat.repository;

import com.eatpizzaquickly.jariotte.domain.seat.entity.Seat;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByConcertId(Long concertId);
}
