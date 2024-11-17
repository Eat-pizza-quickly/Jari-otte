package com.eatpizzaquickly.concertservice.service;


import com.eatpizzaquickly.concertservice.dto.request.VenueCreateRequest;
import com.eatpizzaquickly.concertservice.dto.response.VenueDetailResponse;
import com.eatpizzaquickly.concertservice.entity.Venue;
import com.eatpizzaquickly.concertservice.exception.NotFoundException;
import com.eatpizzaquickly.concertservice.repository.VenueRepository;
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
        Venue venue = venueRepository.findById(venueId).orElseThrow(NotFoundException::new);
        return VenueDetailResponse.from(venue);
    }
}
