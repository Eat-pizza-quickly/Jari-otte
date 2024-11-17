package com.eatpizzaquickly.reservationservice.reservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class KafkaFailedMessage {

    @Id @GeneratedValue
    private Long id;

    private String topic;
    private int kafkaPartition;
    private Long offset;
    private String value;
    private String exceptionMessage;
    private LocalDateTime createdAt;

    @Builder
    private KafkaFailedMessage(String topic, int kafkaPartition, Long offset, String value, String exceptionMessage) {
        this.topic = topic;
        this.kafkaPartition = kafkaPartition;
        this.offset = offset;
        this.value = value;
        this.exceptionMessage = exceptionMessage;
        this.createdAt = LocalDateTime.now();
    }
}
