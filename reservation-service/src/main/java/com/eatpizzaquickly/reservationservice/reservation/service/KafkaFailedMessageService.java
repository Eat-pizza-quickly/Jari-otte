package com.eatpizzaquickly.reservationservice.reservation.service;

import com.eatpizzaquickly.reservationservice.reservation.client.KafkaEventProducer;
import com.eatpizzaquickly.reservationservice.reservation.dto.ReservationCompensationEvent;
import com.eatpizzaquickly.reservationservice.reservation.dto.ReservationCreateRequest;
import com.eatpizzaquickly.reservationservice.reservation.dto.SeatReservationEvent;
import com.eatpizzaquickly.reservationservice.reservation.entity.KafkaFailedMessage;
import com.eatpizzaquickly.reservationservice.reservation.repository.KafkaFailedMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class KafkaFailedMessageService {
    private final KafkaFailedMessageRepository kafkaFailedMessageRepository;
    private final KafkaEventProducer kafkaEventProducer;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveFailedMessage(ConsumerRecord<?, ?> consumerRecord, Exception exception) {
        try {
            KafkaFailedMessage failedMessage = KafkaFailedMessage.builder()
                    .topic(consumerRecord.topic())
                    .kafkaPartition(consumerRecord.partition())
                    .offset(consumerRecord.offset())
                    .value(objectMapper.writeValueAsString(consumerRecord.value()))
                    .exceptionMessage(exception.getMessage())
                    .build();

            kafkaFailedMessageRepository.save(failedMessage);

            log.info("실패한 메시지를 DB에 저장했습니다 : {}", failedMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void produceReservationCompensationEvent(ConsumerRecord<?, ?> consumerRecord) {
        try {
            String value = (String) consumerRecord.value();
            SeatReservationEvent seatReservationEvent = objectMapper.readValue(value, SeatReservationEvent.class);

            ReservationCompensationEvent event = ReservationCompensationEvent.builder()
                    .price(seatReservationEvent.getPrice())
                    .concertId(seatReservationEvent.getConcertId())
                    .seatId(seatReservationEvent.getSeatId())
                    .userId(seatReservationEvent.getUserId())
                    .build();

            kafkaEventProducer.produceReservationCompensationEvent(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
