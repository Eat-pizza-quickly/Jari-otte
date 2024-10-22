package com.eatpizzaquickly.jariotte.domain.seat.service;

import com.eatpizzaquickly.jariotte.domain.seat.dto.resopnse.SeatDto;
import com.eatpizzaquickly.jariotte.domain.seat.dto.resopnse.SeatListResponse;
import com.eatpizzaquickly.jariotte.domain.seat.entity.Seat;
import com.eatpizzaquickly.jariotte.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatListResponse findSeatList(Long concertId) {
        List<Seat> seatList = seatRepository.findByConcertId(concertId);
        List<SeatDto> seatDtoList = seatList.stream().map(SeatDto::from).toList();
        return SeatListResponse.of(seatDtoList);
    }
}
