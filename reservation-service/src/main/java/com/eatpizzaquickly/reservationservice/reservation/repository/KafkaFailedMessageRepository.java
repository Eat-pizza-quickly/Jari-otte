package com.eatpizzaquickly.reservationservice.reservation.repository;

import com.eatpizzaquickly.reservationservice.reservation.entity.KafkaFailedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaFailedMessageRepository extends JpaRepository<KafkaFailedMessage, Long> {
}
