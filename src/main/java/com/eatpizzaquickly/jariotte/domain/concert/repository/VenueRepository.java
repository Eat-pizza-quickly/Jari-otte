package com.eatpizzaquickly.jariotte.domain.concert.repository;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
