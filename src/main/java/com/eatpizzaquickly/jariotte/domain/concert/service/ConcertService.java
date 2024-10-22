package com.eatpizzaquickly.jariotte.domain.concert.service;

import com.eatpizzaquickly.jariotte.domain.concert.dto.reqeuest.ConcertCreateRequest;
import com.eatpizzaquickly.jariotte.domain.concert.dto.response.ConcertDetailResponse;
import com.eatpizzaquickly.jariotte.domain.concert.dto.response.ConcertListResponse;
import com.eatpizzaquickly.jariotte.domain.concert.entity.Category;
import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import com.eatpizzaquickly.jariotte.domain.concert.entity.Venue;
import com.eatpizzaquickly.jariotte.domain.concert.exception.ConcertNotFoundException;
import com.eatpizzaquickly.jariotte.domain.concert.exception.VenueNotFountException;
import com.eatpizzaquickly.jariotte.domain.concert.repository.ConcertRepository;
import com.eatpizzaquickly.jariotte.domain.concert.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;

    @Transactional
    public ConcertDetailResponse saveConcert(ConcertCreateRequest concertCreateRequest) {
        Venue venue = venueRepository.findById(concertCreateRequest.getVenueId()).orElseThrow(VenueNotFountException::new);

        Concert concert = Concert.builder()
                .title(concertCreateRequest.getTitle())
                .description(concertCreateRequest.getDescription())
                .startDate(concertCreateRequest.getStartDate())
                .endDate(concertCreateRequest.getEndDate())
                .category(Category.of(concertCreateRequest.getCategory()))
                .venue(venue)
                .seatCount(venue.getSeatCount())
                .build();

        concertRepository.save(concert);

        return ConcertDetailResponse.from(concert, venue);
    }

    public ConcertDetailResponse findConcert(Long concertId) {
        Concert concert = concertRepository.findById(concertId).orElseThrow(ConcertNotFoundException::new);
        return ConcertDetailResponse.from(concert, concert.getVenue());
    }

    public ConcertListResponse searchConcert(String keyword, Pageable pageable) {
        return null;
    }

    @Transactional
    public void deleteConcert(Long concertId) {
        Concert concert = concertRepository.findById(concertId).orElseThrow(ConcertNotFoundException::new);
        concertRepository.delete(concert);
    }
}
