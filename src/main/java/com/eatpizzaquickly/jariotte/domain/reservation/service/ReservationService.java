package com.eatpizzaquickly.jariotte.domain.reservation.service;

import com.eatpizzaquickly.jariotte.domain.common.exception.NotFoundException;
import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import com.eatpizzaquickly.jariotte.domain.concert.entity.repository.ConcertRepository;
import com.eatpizzaquickly.jariotte.domain.reservation.dto.request.PostReservationRequest;
import com.eatpizzaquickly.jariotte.domain.reservation.dto.response.PostReservationResponse;
import com.eatpizzaquickly.jariotte.domain.reservation.entity.Reservation;
import com.eatpizzaquickly.jariotte.domain.reservation.entity.ReservationStatus;
import com.eatpizzaquickly.jariotte.domain.reservation.repository.ReservationRepository;
import com.eatpizzaquickly.jariotte.domain.seat.entity.Seat;
import com.eatpizzaquickly.jariotte.domain.seat.entity.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    public PostReservationResponse createReservation(Long concertId, Long seatId, PostReservationRequest request) {
        // 유저 검토

        // 콘서트, 좌석 검토
        Concert concert = concertRepository.findById(concertId).orElseThrow(
                () -> new NotFoundException("콘서트가 존재하지 않습니다."));

        Seat seat = seatRepository.findById(seatId).orElseThrow(
                () -> new NotFoundException("좌석이 존재하지 않습니다."));

        // 엔티티 변환
        Reservation reservation = new Reservation(request, ReservationStatus.PENDING, concert, seat);

        // 반환
        return new PostReservationResponse(reservationRepository.save(reservation));
    }
}
