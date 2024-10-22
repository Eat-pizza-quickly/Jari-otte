package com.eatpizzaquickly.jariotte.domain.concert.service;

import com.eatpizzaquickly.jariotte.domain.concert.dto.ConcertSimpleDto;
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
import com.eatpizzaquickly.jariotte.domain.seat.entity.Seat;
import com.eatpizzaquickly.jariotte.domain.seat.repository.SeatRepository;
import com.eatpizzaquickly.jariotte.domain.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;
    private final SeatRepository seatRepository;

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

        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= venue.getSeatCount(); i++) {
            Seat seat = Seat.builder()
                    .seatNumber(i)
                    .concert(concert)
                    .isReserved(false)
                    .build();

            seats.add(seat);
        }
        seatRepository.saveAll(seats);

        return ConcertDetailResponse.from(concert, venue);
    }

    public ConcertDetailResponse findConcert(Long concertId) {
        Concert concert = concertRepository.findByIdWithVenue(concertId).orElseThrow(ConcertNotFoundException::new);
        return ConcertDetailResponse.from(concert, concert.getVenue());
    }

    public ConcertListResponse searchConcert(String keyword, Pageable pageable) {
        Page<Concert> concerts = concertRepository.searchByTitleOrArtists(keyword, pageable);
        List<ConcertSimpleDto> concertSimpleDtoList = concerts.map(ConcertSimpleDto::from).toList();
        return ConcertListResponse.of(concertSimpleDtoList);
    }

    @Transactional
    public void deleteConcert(Long concertId) {
        Concert concert = concertRepository.findById(concertId).orElseThrow(ConcertNotFoundException::new);
        concertRepository.delete(concert);
    }
}
