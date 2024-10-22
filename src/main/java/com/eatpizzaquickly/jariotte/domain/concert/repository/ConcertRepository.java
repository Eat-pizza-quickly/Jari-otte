package com.eatpizzaquickly.jariotte.domain.concert.repository;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConcertRepository extends JpaRepository<Concert, Long>, ConcertCustomRepository {

    @Query("SELECT c FROM Concert c JOIN FETCH c.venue WHERE c.id = :id")
    Optional<Concert> findByIdWithVenue(@Param("id") Long id);
}
