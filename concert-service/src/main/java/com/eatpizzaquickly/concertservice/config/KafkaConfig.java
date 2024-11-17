package com.eatpizzaquickly.concertservice.config;

import com.eatpizzaquickly.concertservice.service.KafkaFailedMessageService;
import com.eatpizzaquickly.concertservice.util.SlackNotifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Configuration
@EnableKafka
public class KafkaConfig {

    private final KafkaFailedMessageService kafkaFailedMessageService;
    private final SlackNotifier slackNotifier;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> myconfig = new HashMap<>();
        myconfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        myconfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        myconfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        myconfig.put(ProducerConfig.RETRIES_CONFIG, 3);
        myconfig.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 3000);
        myconfig.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 30000);
        return new DefaultKafkaProducerFactory<>(myconfig);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> myConfig = new HashMap<>();
        myConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        myConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        myConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        myConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        return new DefaultKafkaConsumerFactory<>(myConfig);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        kafkaListenerContainerFactory.setCommonErrorHandler(getErrorHandler());
        kafkaListenerContainerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return kafkaListenerContainerFactory;
    }

    @Bean
    public DefaultErrorHandler getErrorHandler() {
        long interval = 3000L;
        long maxAttempts = 3L;

        FixedBackOff fixedBackOff = new FixedBackOff(interval, maxAttempts);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, exception) -> {
            try {
                log.error("재시도 초과. message: {}, exception: {}", consumerRecord.value(), exception.getMessage());

                // 슬랙 알림 발송
                notifyKafkaError(consumerRecord, exception);

                // 실패 메시지 저장
                kafkaFailedMessageService.saveConsumeFailedMessage(consumerRecord, exception);

            } catch (Exception ex) {
                log.error("Kafka 에러 핸들링 실패: {}", ex.getMessage());
            }

        }, fixedBackOff);

        errorHandler.setAckAfterHandle(true);

        errorHandler.addRetryableExceptions(
                HttpServerErrorException.class
        );

        errorHandler.addNotRetryableExceptions(
                JsonProcessingException.class,
                DataIntegrityViolationException.class,
                IllegalArgumentException.class
        );
        return errorHandler;
    }

    private void notifyKafkaError(ConsumerRecord<?, ?> consumerRecord, Exception exception) {
        String message = String.format(
                """
                        [Kafka 메시지 처리 실패]
                        - Topic: %s
                        - Partition: %d
                        - Offset: %d
                        - Message: %s
                        - Exception: %s
                        """,
                consumerRecord.topic(),
                consumerRecord.partition(),
                consumerRecord.offset(),
                consumerRecord.value(),
                exception.getMessage()
        );

        slackNotifier.sendNotification(message);
    }

    private void notifyKafkaErrorHandlingFailure(ConsumerRecord<?, ?> consumerRecord, Exception exception) {
        String message = String.format(
                """
                [Kafka 에러 핸들링 실패]
                - Topic: %s
                - Partition: %d
                - Offset: %d
                - Message: %s
                - Exception: %s
                """,
                consumerRecord.topic(),
                consumerRecord.partition(),
                consumerRecord.offset(),
                consumerRecord.value(),
                exception.getMessage()
        );

        slackNotifier.sendNotification(message);
    }

}
