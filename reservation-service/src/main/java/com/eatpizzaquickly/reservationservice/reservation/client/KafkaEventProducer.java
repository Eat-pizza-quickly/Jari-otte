package com.eatpizzaquickly.reservationservice.reservation.client;

import com.eatpizzaquickly.reservationservice.common.util.SlackNotifier;
import com.eatpizzaquickly.reservationservice.reservation.dto.ReservationCompensationEvent;
import com.eatpizzaquickly.reservationservice.reservation.exception.CompensationEventPublishingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
@Component
public class KafkaEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SlackNotifier slackNotifier;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.reservation-created-compensation}")
    private String reservationCompensationTopic;

    public void produceReservationCompensationEvent(ReservationCompensationEvent event) throws JsonProcessingException {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(reservationCompensationTopic, event);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("보상 트랜잭션 이벤트 발행 성공: {}", event.toString());
                notifySuccessMessage(event);
            } else {
                log.error("보상 트랜잭션  이벤트 발행 실패: {}, Exception: {}", event.toString(), ex.getMessage());
                throw new CompensationEventPublishingException();
            }
        });
    }

    private void notifySuccessMessage(ReservationCompensationEvent event) {
        String message = String.format(
                """
                        [보상 트랜잭션 이벤트 발행 성공]
                        - Event: %s
                        """,
                event.toString()
        );

        slackNotifier.sendNotification(message);
    }

    private void notifyFailureMessage(ReservationCompensationEvent event, Throwable ex) {
        String message = String.format(
                """
                        [보상 트랜잭션 이벤트 발행 실패]
                        - Event: %s
                        - Error: %s
                        """,
                event.toString(),
                ex.getMessage()
        );

        slackNotifier.sendNotification(message);
    }
}
