package com.eatpizzaquickly.reservationservice.reservation.client;

import com.eatpizzaquickly.reservationservice.reservation.dto.ReservationCreateRequest;
import com.eatpizzaquickly.reservationservice.reservation.dto.SeatReservationEvent;
import com.eatpizzaquickly.reservationservice.reservation.service.ReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KafkaEventConsumer {

    private final ReservationService reservationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${spring.kafka.topic.reservation-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeSeatReservationEvent(String message) throws JsonProcessingException {
        SeatReservationEvent event = objectMapper.readValue(message, SeatReservationEvent.class);
        ReservationCreateRequest request = ReservationCreateRequest.from(event);
        reservationService.createReservation(request);
    }
}
