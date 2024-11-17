package com.eatpizzaquickly.concertservice.repository;

import com.eatpizzaquickly.concertservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByConcertId(Long concertId);

    // 예약되지 않은 좌석 조회 쿼리
    @Query("SELECT s FROM Seat s WHERE s.concert.id = :concertId AND s.isReserved = false")
    List<Seat> findAvailableSeatsByConcertId(@Param("concertId") Long concertId);
}
