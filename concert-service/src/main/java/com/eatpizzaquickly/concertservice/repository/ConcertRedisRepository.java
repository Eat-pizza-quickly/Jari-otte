package com.eatpizzaquickly.concertservice.repository;

import com.eatpizzaquickly.concertservice.dto.response.SeatListResponse;
import com.eatpizzaquickly.concertservice.exception.detail.RedisException;
import com.eatpizzaquickly.concertservice.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class ConcertRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;

    // 조회수 증가
    public void increaseViewCount(Long concertId) {
        RScoredSortedSet<Long> sortedSet = redissonClient.getScoredSortedSet(RedisUtil.getViewCountKey());
        sortedSet.addScore(concertId, 1);
    }

    // 상위 조회수 콘서트 조회
    public List<Long> getTopViewedConcertIds(int limit) {
        RScoredSortedSet<Long> sortedSet = redissonClient.getScoredSortedSet(RedisUtil.getViewCountKey());
        Collection<Long> concertIds = sortedSet.valueRangeReversed(0, limit - 1);
        return new ArrayList<>(concertIds);
    }

    // 예약 가능한 좌석을 Redis에 추가
    public void addAvailableSeats(Long concertId, List<Long> seatIds) {
        RSet<String> availableSeats = redissonClient.getSet(RedisUtil.getAvailableSeatsKey(concertId));
        for (Long seatId : seatIds) {
            availableSeats.add(String.valueOf(seatId));
        }
    }

    // Redis에서 잔여 좌석 수 조회
    public int getAvailableSeatCount(Long concertId) {
        String redisKey = RedisUtil.getAvailableSeatsKey(concertId);
        RSet<String> availableSeats = redissonClient.getSet(redisKey);
        return availableSeats.size();
    }

    // 좌석을 다시 Redis에 추가 (예약 실패 시)
    public void addSeatBackToAvailable(Long concertId, Long seatId) {
        String redisKey = RedisUtil.getAvailableSeatsKey(concertId);
        RSet<String> availableSeats = redissonClient.getSet(redisKey);
        availableSeats.add(String.valueOf(seatId));
    }

    public void reserveSeat(Long concertId, Long seatId) {
        String availableSeatsKey = RedisUtil.getAvailableSeatsKey(concertId);
        String seatValue = String.valueOf(seatId);

        String result = redisTemplate.execute(
                reserveSeatScript(),
                List.of(availableSeatsKey),
                seatValue
        );

        SeatReservationCode.checkReservationResult(SeatReservationCode.find(result));
    }

    private RedisScript<String> reserveSeatScript() {
        String script = """
                if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 1 then
                    redis.call('SREM', KEYS[1], ARGV[1]) -- 좌석을 Set에서 제거
                    return '1' -- 성공적으로 예약됨
                else
                    return '2' -- 이미 예약된 좌석
                end
                """;
        return RedisScript.of(script, String.class);
    }

    // Redis 에서 좌석 데이터가 있는지 확인
    public boolean hasAvailableSeats(Long concertId) {
        String availableSeatsKey = RedisUtil.getAvailableSeatsKey(concertId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(availableSeatsKey));
    }
}
