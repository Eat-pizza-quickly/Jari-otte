package com.eatpizzaquickly.couponservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponEventProducer {

    private final KafkaTemplate<String, CouponEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.coupon-events}")
    private String topicName;

    public void sendCouponEvent(CouponEvent event) {
        try {
            kafkaTemplate.send(topicName, event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Coupon event sent successfully: {} with offset: {}",
                                    event, result.getRecordMetadata().offset());
                        } else {
                            log.error("Failed to send coupon event: {}", event, ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Error occurred while sending coupon event: {}", event, e);
            throw e;
        }
    }

}
