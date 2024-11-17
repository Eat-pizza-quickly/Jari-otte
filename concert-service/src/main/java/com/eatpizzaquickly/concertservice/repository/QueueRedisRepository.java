package com.eatpizzaquickly.concertservice.repository;

import com.eatpizzaquickly.concertservice.exception.detail.RedisException;
import com.eatpizzaquickly.concertservice.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class QueueRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final int MAX_REQUESTS_PER_SECOND = 100;

    public boolean isRequestLimitExceeded(Long concertId) {
        String requestKey = RedisUtil.getRequestCountKey(concertId);

        Long requestCount = redisTemplate.opsForValue().increment(requestKey); // 요청 수 증가

        if (requestCount == null) {
            throw new RedisException();
        }

        if(requestCount == 1){
            redisTemplate.expire(requestKey, Duration.ofSeconds(1)); // TTL 1초 설정
        }

        return requestCount > MAX_REQUESTS_PER_SECOND;
    }

    public void addToQueue(Long concertId, Long userId) {
        String queueKey = RedisUtil.getQueueKey(concertId);
        Long timestamp = getCurrentSecond();
        redisTemplate.opsForZSet().add(queueKey, String.valueOf(userId), timestamp); // 대기열에 추가
    }

    public Integer getQueuePosition(Long concertId, Long userId) {
        String queueKey = RedisUtil.getQueueKey(concertId);
        Long position = redisTemplate.opsForZSet().rank(queueKey, String.valueOf(userId));

        if (position == null) {
            return null;
        }

        return position.intValue() + 1;
    }

    public void markInReservation(Long concertId, Long userId) {
        String inReservationKey = RedisUtil.getInReservationKey(concertId);
        redisTemplate.opsForSet().add(inReservationKey, String.valueOf(userId));
    }

    public boolean isInReservation(Long concertId, Long userId) {
        String inReservationKey = RedisUtil.getInReservationKey(concertId);
        Boolean isMember = redisTemplate.opsForSet().isMember(inReservationKey, String.valueOf(userId));
        return isMember != null && isMember;
    }

    public void removeFromReservation(Long concertId, Long userId) {
        String inReservationKey = RedisUtil.getInReservationKey(concertId);
        redisTemplate.opsForSet().remove(inReservationKey, String.valueOf(userId));
    }

    public Long getNextUserFromQueue(Long concertId) {
        String queueKey = RedisUtil.getQueueKey(concertId);

        // 대기열에서 첫 번째 사용자 가져오기
        Set<String> nextUsers = redisTemplate.opsForZSet().range(queueKey, 0, 0);
        if (nextUsers == null || nextUsers.isEmpty()) {
            return null; // 대기열이 비어있다면 null 반환
        }

        Long nextUserId = Long.valueOf(nextUsers.iterator().next());

        // 대기열에서 해당 사용자 제거
        redisTemplate.opsForZSet().remove(queueKey, nextUserId);

        // "좌석 예매 중" 상태에 사용자 추가
        markInReservation(concertId, nextUserId);

        return nextUserId; // 다음 사용자 ID 반환
    }

    // Redis 대기열이 비어 있는지 확인하는 메서드
    public boolean isQueueEmpty(Long concertId) {
        String queueKey = RedisUtil.getQueueKey(concertId);
        Long queueSize = redisTemplate.opsForZSet().zCard(queueKey);
        return queueSize == null || queueSize == 0; // null 인 경우를 처리
    }

    private Long getCurrentSecond() {
        return System.currentTimeMillis() / 1000;
    }
}
