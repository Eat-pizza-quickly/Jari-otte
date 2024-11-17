package com.eatpizzaquickly.notificationservice.kafka.producer;

import com.eatpizzaquickly.notificationservice.kafka.dto.CouponEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponEventProducer {
    @Qualifier("stringKafkaTemplate")  // 특정 KafkaTemplate bean 지정
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendCouponEvent(String message) {
        kafkaTemplate.send("coupon-events", message);
    }
}