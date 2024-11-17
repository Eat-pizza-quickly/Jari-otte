package com.eatpizzaquickly.concertservice.repository;

import com.eatpizzaquickly.concertservice.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
