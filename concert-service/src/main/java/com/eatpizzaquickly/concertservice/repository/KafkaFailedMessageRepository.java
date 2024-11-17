package com.eatpizzaquickly.concertservice.repository;

import com.eatpizzaquickly.concertservice.entity.KafkaFailedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaFailedMessageRepository extends JpaRepository<KafkaFailedMessage, Long> {
}
