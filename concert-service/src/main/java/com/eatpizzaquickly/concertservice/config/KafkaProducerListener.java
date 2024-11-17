package com.eatpizzaquickly.concertservice.config;

import com.eatpizzaquickly.concertservice.service.KafkaFailedMessageService;
import com.eatpizzaquickly.concertservice.util.SlackNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducerListener implements ProducerListener<String, Object> {

    private final KafkaFailedMessageService kafkaFailedMessageService;
    private final SlackNotifier slackNotifier;

    @Override
    public void onError(ProducerRecord<String, Object> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("Message Failed to send: {}", exception.getMessage());

        kafkaFailedMessageService.saveProduceFailedMessage(producerRecord, exception);

        String slackMessage = String.format(
                "Kafka 메시지 처리 실패!%nMessage: %s%nException: %s",
                producerRecord.value(), exception.getMessage()
        );

        slackNotifier.sendNotification(slackMessage);
    }
}
