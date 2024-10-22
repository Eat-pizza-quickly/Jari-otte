package com.eatpizzaquickly.jariotte.domain.concert.repository;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
