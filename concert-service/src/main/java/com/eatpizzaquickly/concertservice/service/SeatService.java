package com.eatpizzaquickly.concertservice.service;

import com.eatpizzaquickly.concertservice.client.KafkaEventProducer;
import com.eatpizzaquickly.concertservice.client.ReservationCompensationEvent;
import com.eatpizzaquickly.concertservice.client.ReservationCreateRequest;
import com.eatpizzaquickly.concertservice.dto.SeatDto;
import com.eatpizzaquickly.concertservice.dto.SeatReservationEvent;
import com.eatpizzaquickly.concertservice.dto.request.SeatReservationRequest;
import com.eatpizzaquickly.concertservice.dto.response.ApiResponse;
import com.eatpizzaquickly.concertservice.dto.response.SeatListResponse;
import com.eatpizzaquickly.concertservice.entity.Concert;
import com.eatpizzaquickly.concertservice.entity.Seat;
import com.eatpizzaquickly.concertservice.exception.NotFoundException;
import com.eatpizzaquickly.concertservice.exception.detail.CompensateReservationFailureException;
import com.eatpizzaquickly.concertservice.exception.detail.InvalidReservationFlowException;
import com.eatpizzaquickly.concertservice.exception.detail.RequestLimitExceededException;
import com.eatpizzaquickly.concertservice.repository.ConcertRedisRepository;
import com.eatpizzaquickly.concertservice.repository.ConcertRepository;
import com.eatpizzaquickly.concertservice.repository.QueueRedisRepository;
import com.eatpizzaquickly.concertservice.repository.SeatRepository;
import com.eatpizzaquickly.concertservice.util.SlackNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SeatService {

    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;
    private final ConcertRedisRepository concertRedisRepository;
    private final QueueRedisRepository queueRedisRepository;
    private final KafkaEventProducer kafkaEventProducer;
    private final SlackNotifier slackNotifier;

    public SeatListResponse findSeatList(Long concertId, Long userId) {
        // 사용자가 "좌석 예매 중" 상태에 있는지 먼저 확인
        if (queueRedisRepository.isInReservation(concertId, userId)) {
            return fetchSeatList(concertId);
        }

        if (queueRedisRepository.isRequestLimitExceeded(concertId)) {
            queueRedisRepository.addToQueue(concertId, userId);
            throw new RequestLimitExceededException(concertId);
        }

        // "좌석 예매 중" 마크
        queueRedisRepository.markInReservation(concertId, userId);

        // Redis 에 좌석 데이터가 없으면 DB 에서 다시 로드
        if (!concertRedisRepository.hasAvailableSeats(concertId)) {
            reloadSeatsFromDatabase(concertId);
        }

        return fetchSeatList(concertId);
    }

    @Transactional
    public void reserveSeat(Long userId, Long concertId, Long seatId, SeatReservationRequest request) {
        if (!queueRedisRepository.isInReservation(concertId, userId)) {
            throw new InvalidReservationFlowException();
        }

        // Redis 의 Lua script 를 이용한 원자적 좌석 예약 처리
        concertRedisRepository.reserveSeat(concertId, seatId);

        try {
            // Seat DB 조회 및 예약 상태 변경
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new NotFoundException("좌석이 존재하지 않습니다."));
            seat.changeReserved(true);

//            concertRepository.findById(concertId).orElseThrow(() -> new NotFoundException("콘서트가 존재하지 않습니다."));

            SeatReservationEvent reservationEvent = SeatReservationEvent.builder()
                    .userId(userId)
                    .seatId(seatId)
                    .concertId(concertId)
                    .price(request.getPrice())
                    .build();

            kafkaEventProducer.produceSeatReservationEvent(reservationEvent);

            queueRedisRepository.removeFromReservation(concertId, userId);

            processNextUserInQueue(concertId);

        } catch (Exception e) {
            concertRedisRepository.addSeatBackToAvailable(concertId, seatId);
            queueRedisRepository.markInReservation(concertId, userId);
            throw new RuntimeException("좌석 예약 중 오류가 발생했습니다.");
        }
    }


    private void reloadSeatsFromDatabase(Long concertId) {
        List<Seat> availableSeats = seatRepository.findAvailableSeatsByConcertId(concertId);
        List<Long> availableSeatIds = availableSeats.stream().map(Seat::getId).toList();
        concertRedisRepository.addAvailableSeats(concertId, availableSeatIds);
    }

    private void processNextUserInQueue(Long concertId) {
        if (queueRedisRepository.isQueueEmpty(concertId)) {
            return; // 대기열이 비어있으면 아무 작업도 수행하지 않음
        }

        Long nextUserId = queueRedisRepository.getNextUserFromQueue(concertId);
        if (nextUserId != null) {
            queueRedisRepository.markInReservation(concertId, nextUserId);
        }
    }

    private SeatListResponse fetchSeatList(Long concertId) {
        // Redis 에 좌석 데이터가 없으면 DB 에서 다시 로드
        if (!concertRedisRepository.hasAvailableSeats(concertId)) {
            reloadSeatsFromDatabase(concertId);
        }

        List<Seat> seatList = seatRepository.findByConcertId(concertId);
        List<SeatDto> seatDtoList = seatList.stream().map(SeatDto::from).toList();
        return SeatListResponse.of(seatDtoList);
    }

    // Redis 상태를 DB로 동기화
    @Transactional
    public void syncAvailableSeatsToDatabase(Long concertId) {
        int availableSeatCount = concertRedisRepository.getAvailableSeatCount(concertId);

        // DB 에서 Concert 엔티티 조회
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new NotFoundException("콘서트를 찾을 수 없습니다."));

        // 잔여 좌석 수 반영
        concert.updateSeatCount(availableSeatCount);
        concertRepository.save(concert); // 업데이트된 좌석 수 저장
    }

    @Transactional
    public void compensateReservation(ReservationCompensationEvent event) {
        Long concertId = event.getConcertId();
        Long seatId = event.getSeatId();
        Long userId = event.getUserId();

        log.info("보상 트랜잭션 시작 - concertId: {}, seatId: {}, userId: {}", concertId, seatId, userId);

        try {
            // 1. Redis 상태 복구
            concertRedisRepository.addSeatBackToAvailable(concertId, seatId);
            queueRedisRepository.markInReservation(concertId, userId);

            // 2. DB 상태 복구
            Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new NotFoundException("해당 좌석이 존재하지 않습니다."));
            seat.changeReserved(false);

            log.info("보상 트랜잭션 완료 - concertId: {}, seatId: {}, userId: {}", concertId, seatId, userId);
            notifyCompensateReservationSuccess(event);
        } catch (Exception e) {
            log.info("보상 트랜잭션 실패 - concertId: {}, seatId: {}, userId: {}", concertId, seatId, userId);
            throw new CompensateReservationFailureException();
        }
    }

    private void notifyCompensateReservationSuccess(ReservationCompensationEvent event) {
        String message = String.format(
                """
                        [보상 트랜잭션 완료]
                        - Concert ID: %d
                        - Seat ID: %d
                        - User ID: %d
                        """,
                event.getConcertId(), event.getSeatId(), event.getUserId()
        );

        slackNotifier.sendNotification(message);
    }
}
