package com.eatpizzaquickly.concertservice.service;

import com.eatpizzaquickly.concertservice.dto.response.QueueResponse;
import com.eatpizzaquickly.concertservice.exception.detail.UserNotInQueueException;
import com.eatpizzaquickly.concertservice.repository.QueueRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QueueService {

    private final QueueRedisRepository queueRedisRepository;

    public QueueResponse findPosition(Long concertId, Long userId) {

        String url = "/api/v1/concerts/%s/seats".formatted(concertId);

        // 예약 상태 우선 확인
        if (queueRedisRepository.isInReservation(concertId, userId)) {
            return new QueueResponse(0, url, true);
        }

        // 대기열 위치 확인
        Integer position = queueRedisRepository.getQueuePosition(concertId, userId);
        if (position == null) {
            throw new UserNotInQueueException();
        }

        return new QueueResponse(position, url, false);
    }
}
