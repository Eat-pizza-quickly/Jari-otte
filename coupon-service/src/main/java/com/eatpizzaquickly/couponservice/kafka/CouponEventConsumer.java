package com.eatpizzaquickly.couponservice.kafka;

import com.eatpizzaquickly.couponservice.exception.DuplicateCouponException;
import com.eatpizzaquickly.couponservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponEventConsumer {
    private final CouponService couponService;

    @KafkaListener(topics = "${spring.kafka.topic.coupon-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCouponEvent(CouponEvent event) {
        log.info("Received coupon event: {}", event);
        try {
            switch (event.getEventType()) {
                case "SINGLE_ISSUE":
                    couponService.issueCouponToUser(event.getUserId(), event.getCouponCode());
                    break;
                case "BULK_ISSUE":
                    couponService.issueCouponToAllUsers(event.getCouponId());
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (DuplicateCouponException e) {
            // 중복 발급 예외 발생 시 로그를 남기고 이벤트를 무시
            log.warn("Duplicate coupon issuance detected for userId: {}, couponCode: {}. Ignoring this event.",
                    event.getUserId(), event.getCouponCode());
            // 예외를 던지지 않아 Kafka 재처리가 발생하지 않도록 합니다.
        } catch (Exception e) {
            // 다른 예외 발생 시 에러를 로깅하고 예외를 다시 던져 Kafka가 재처리하게 함
            log.error("Error processing coupon event: {}", event, e);
            throw e;  // 재처리 로직 또는 DLQ로 이동 가능
        }
    }
}