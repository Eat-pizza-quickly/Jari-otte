package com.eatpizzaquickly.notificationservice.kafka.producer;

import com.eatpizzaquickly.notificationservice.kafka.dto.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {
    @Qualifier("stringKafkaTemplate")  // 특정 KafkaTemplate bean 지정
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendPaymentEvent(String message) {
        kafkaTemplate.send("payment-events", message);
    }
}