package com.eatpizzaquickly.concertservice.service;

import com.eatpizzaquickly.concertservice.entity.KafkaFailedMessage;
import com.eatpizzaquickly.concertservice.repository.KafkaFailedMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class KafkaFailedMessageService {
    private final KafkaFailedMessageRepository kafkaFailedMessageRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveProduceFailedMessage(ProducerRecord<?, ?> record, Exception exception) {
        try {
            KafkaFailedMessage failedMessage = KafkaFailedMessage.builder()
                    .topic(record.topic())
                    .kafkaPartition(record.partition())
                    .value(objectMapper.writeValueAsString(record.value()))
                    .exceptionMessage(exception.getMessage())
                    .build();

            kafkaFailedMessageRepository.save(failedMessage);

            log.info("실패한 메시지를 DB에 저장했습니다 : {}", failedMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveConsumeFailedMessage(ConsumerRecord<?, ?> record, Exception exception) {
        try {
            KafkaFailedMessage failedMessage = KafkaFailedMessage.builder()
                    .topic(record.topic())
                    .kafkaPartition(record.partition())
                    .offset(record.offset())
                    .value(objectMapper.writeValueAsString(record.value()))
                    .exceptionMessage(exception.getMessage())
                    .createdAt(LocalDateTime.now())
                    .build();

            kafkaFailedMessageRepository.save(failedMessage);

            log.info("실패한 메시지를 DB에 저장했습니다 : {}", failedMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
