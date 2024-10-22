package com.eatpizzaquickly.jariotte.domain.concert.service;

import com.eatpizzaquickly.jariotte.domain.concert.dto.reqeuest.VenueCreateRequest;
import com.eatpizzaquickly.jariotte.domain.concert.dto.response.VenueDetailResponse;
import com.eatpizzaquickly.jariotte.domain.concert.entity.Venue;
import com.eatpizzaquickly.jariotte.domain.concert.exception.VenueNotFountException;
import com.eatpizzaquickly.jariotte.domain.concert.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VenueService {

    private final VenueRepository venueRepository;

    @Transactional
    public VenueDetailResponse saveVenue(VenueCreateRequest venueCreateRequest) {
        Venue venue = Venue.builder()
                .venueName(venueCreateRequest.getVenueName())
                .location(venueCreateRequest.getLocation())
                .seatCount(venueCreateRequest.getSeatCount())
                .build();
        venueRepository.save(venue);

        return VenueDetailResponse.from(venue);
    }

    public VenueDetailResponse findVenue(Long venueId) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(VenueNotFountException::new);
        return VenueDetailResponse.from(venue);
    }
}
