package com.eatpizzaquickly.notificationservice.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaConsumerConfig(@Qualifier("stringKafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 폴링 시 최대 메시지 개수
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        // 재시도 관련 설정: Consumer 자체에서 에러 발생 시 특정 타이밍에 재시도
        props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 1000); // 첫 번째 실패 후 재시도 대기 시간
        props.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, 1000); // 연결 재시도 간격
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> KafkaListenerContainerFactory() {  // 메서드명 수정
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        // MANUAL 확인 모드 설정
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        // 에러 핸들러 설정
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate,
                        (record, ex) -> {
                            log.error("메시지 처리 실패. DLT로 이동 - topic: {}, offset: {}, exception: {}",
                                    record.topic(), record.offset(), ex.getMessage());
                            return new TopicPartition(
                                    record.topic() + ".DLT",
                                    record.partition()
                            );
                        }
                ),
                new ExponentialBackOff(1000L, 2.0)  // 1초부터 시작해서 2배씩 증가
        );

        // 특정 예외는 재시도하지 않고 바로 DLT로
        errorHandler.addNotRetryableExceptions(
                IllegalArgumentException.class,    // 잘못된 입력값
                org.springframework.messaging.converter.MessageConversionException.class  // 변환 에러
        );

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}