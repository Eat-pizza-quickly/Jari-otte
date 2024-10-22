package com.eatpizzaquickly.jariotte.domain.reservation.service;

import com.eatpizzaquickly.jariotte.domain.common.exception.NotFoundException;
import com.eatpizzaquickly.jariotte.domain.common.exception.UnauthorizedException;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public PostReservationResponse createReservation(Long concertId, Long seatId, PostReservationRequest request) {
        // 유저 검토

        // 콘서트, 좌석 검토
        Concert concert = concertRepository.findById(concertId).orElseThrow(
                () -> new NotFoundException("콘서트가 존재하지 않습니다."));

        Seat seat = seatRepository.findById(seatId).orElseThrow(
                () -> new NotFoundException("좌석이 존재하지 않습니다."));

        // 엔티티 변환
        Reservation reservation = Reservation.builder()
                .price(request.getPrice())
                .reservationStatus(ReservationStatus.PENDING)
                .concert(concert)
                .build();
        // 좌석 예약상태로 변경
        reservation.setSeatToReserved(seat);
        // 저장 후 반환
        Reservation savedReservation = reservationRepository.save(reservation);
        return PostReservationResponse.from(savedReservation);
    }

    @Transactional
    public void cancelReservation(String email, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new NotFoundException("예약이 존재하지 않습니다."));
        String owner = reservation.getUser().getEmail();

        if (!owner.equals(email)) {
            throw new UnauthorizedException("예약한 유저가 아닙니다.");
        }

        reservation.setSeatToCancelled(reservation.getSeat());
        reservation.setStatus(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);
    }
}
