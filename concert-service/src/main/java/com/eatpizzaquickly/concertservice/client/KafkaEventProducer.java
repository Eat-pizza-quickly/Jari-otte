package com.eatpizzaquickly.concertservice.client;

import com.eatpizzaquickly.concertservice.dto.SeatReservationEvent;
import com.eatpizzaquickly.concertservice.exception.detail.ReservationEventPublishingException;
import com.eatpizzaquickly.concertservice.service.KafkaFailedMessageService;
import com.eatpizzaquickly.concertservice.util.SlackNotifier;
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

    @Value("${spring.kafka.topic.reservation-created}")
    private String seatReservationCreatedTopic;

    public void produceSeatReservationEvent(SeatReservationEvent event){
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(seatReservationCreatedTopic, event);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("좌석 예매 이벤트 발행 실패: {}, Exception: {}", event.toString(), ex.getMessage());
                notifyFailureMessage(event, ex);
                throw new ReservationEventPublishingException();
            }
        });
    }

    private void notifyFailureMessage(SeatReservationEvent event, Throwable ex) {
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
