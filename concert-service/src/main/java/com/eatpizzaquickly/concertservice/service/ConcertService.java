package com.eatpizzaquickly.concertservice.service;

import com.eatpizzaquickly.concertservice.dto.ConcertSimpleDto;
import com.eatpizzaquickly.concertservice.dto.request.ConcertCreateRequest;
import com.eatpizzaquickly.concertservice.dto.response.ConcertDetailResponse;
import com.eatpizzaquickly.concertservice.dto.response.ConcertListResponse;
import com.eatpizzaquickly.concertservice.entity.Category;
import com.eatpizzaquickly.concertservice.entity.Concert;
import com.eatpizzaquickly.concertservice.entity.Seat;
import com.eatpizzaquickly.concertservice.entity.Venue;
import com.eatpizzaquickly.concertservice.exception.NotFoundException;
import com.eatpizzaquickly.concertservice.repository.ConcertRedisRepository;
import com.eatpizzaquickly.concertservice.repository.ConcertRepository;
import com.eatpizzaquickly.concertservice.repository.SeatRepository;
import com.eatpizzaquickly.concertservice.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;
    private final SeatRepository seatRepository;
    private final ConcertRedisRepository concertRedisRepository;
    private final SearchService searchService;

    @Transactional
    public ConcertDetailResponse saveConcert(ConcertCreateRequest concertCreateRequest, Long hostId) {
        Venue venue = venueRepository.findById(concertCreateRequest.getVenueId()).orElseThrow(NotFoundException::new);

        Concert concert = Concert.builder()
                .title(concertCreateRequest.getTitle())
                .hostId(hostId)
                .description(concertCreateRequest.getDescription())
                .artists(concertCreateRequest.getArtists())
                .startDate(concertCreateRequest.getStartDate())
                .endDate(concertCreateRequest.getEndDate())
                .category(Category.of(concertCreateRequest.getCategory()))
                .thumbnailUrl(concertCreateRequest.getThumbnailUrl())
                .venue(venue)
                .seatCount(venue.getSeatCount())
                .build();

        concertRepository.save(concert);

        // Elasticsearch 인덱스 저장
        try {
            searchService.saveIndex(concert);
        } catch (Exception e) {
            System.err.println("Elasticsearch 인덱싱 실패: " + e.getMessage());
        }

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

        // Redis에 예약 가능한 좌석 세팅
        List<Long> seatIds = seats.stream().map(Seat::getId).toList();
        concertRedisRepository.addAvailableSeats(concert.getId(), seatIds);

        return ConcertDetailResponse.from(concert, venue, venue.getSeatCount());
    }

    public ConcertListResponse findAllConcerts(Pageable pageable) {
        List<ConcertSimpleDto> concertSimpleDtoList = concertRepository.findAll(pageable).map(ConcertSimpleDto::from).toList();
        return ConcertListResponse.of(concertSimpleDtoList);
    }

    public ConcertDetailResponse findConcert(Long concertId) {
        Concert concert = concertRepository.findByIdWithVenue(concertId).orElseThrow(NotFoundException::new);
        increaseViewCount(concertId);

        // Redis 에 좌석 데이터가 없으면 DB 에서 다시 로드
        if (!concertRedisRepository.hasAvailableSeats(concertId)) {
            reloadSeatsFromDatabase(concertId);
        }

        int availableSeatCount = concertRedisRepository.getAvailableSeatCount(concertId);

        return ConcertDetailResponse.from(concert, concert.getVenue(), availableSeatCount);
    }

    // 삭제
//    public ConcertListResponse searchConcert(String keyword, Pageable pageable) {
//        Page<Concert> concerts = concertRepository.searchByTitleOrArtists(keyword, pageable);
//        List<ConcertSimpleDto> concertSimpleDtoList = concerts.map(ConcertSimpleDto::from).toList();
//        return ConcertListResponse.of(concertSimpleDtoList);
//    }

    @Transactional
    public void deleteConcert(Long concertId) {
        Concert concert = concertRepository.findById(concertId).orElseThrow(NotFoundException::new);
        concertRepository.delete(concert);

        // 인덱스 삭제
        try {
            searchService.delIndex(concert.getId());
        } catch (Exception e) {
            System.err.println("Elasticsearch 인덱싱 실패: " + e.getMessage());
        }
    }

    public Page<ConcertSimpleDto> searchByCategory(String category, Pageable pageable) {
        Category vaildCategory = Category.of(category);
        Page<Concert> concert = concertRepository.findAllByCategory(vaildCategory, pageable);
        return concert.map(ConcertSimpleDto::from);
    }

    @Cacheable(value = "topViewedConcerts", key = "#limit", unless = "#result.size() == 0")
    public List<ConcertSimpleDto> getTopViewedConcerts(int limit) {
        List<Long> topViewedConcertIds = concertRedisRepository.getTopViewedConcertIds(limit);
        List<Concert> concerts = concertRepository.findAllById(topViewedConcertIds);
        return concerts.stream().map(ConcertSimpleDto::from).toList();
    }

    private void increaseViewCount(Long concertId) {
        concertRedisRepository.increaseViewCount(concertId);
    }


    @Transactional(readOnly = true)
    public Long findHostIdByConcertId(Long concertId) {
        log.info("콘서트 ID : {}",concertId);
        Concert concert = concertRepository.findById(concertId).orElseThrow(
                () -> new NotFoundException("콘서트가 없습니다.")
        );
        return concert.getHostId();
    }

    private void reloadSeatsFromDatabase(Long concertId) {
        List<Seat> availableSeats = seatRepository.findAvailableSeatsByConcertId(concertId);
        List<Long> availableSeatIds = availableSeats.stream().map(Seat::getId).toList();
        concertRedisRepository.addAvailableSeats(concertId, availableSeatIds);
    }
}
