package com.eatpizzaquickly.notificationservice.kafka.consumer;

import com.eatpizzaquickly.notificationservice.entity.FailMessage;
import com.eatpizzaquickly.notificationservice.exception.EmailSendingException;
import com.eatpizzaquickly.notificationservice.exception.InvalidEmailException;
import com.eatpizzaquickly.notificationservice.exception.ProcessingException;
import com.eatpizzaquickly.notificationservice.kafka.dto.CouponEvent;
import com.eatpizzaquickly.notificationservice.repository.FailedMessageRepository;
import com.eatpizzaquickly.notificationservice.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.Acknowledgment;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponEventConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final FailedMessageRepository failedMessageRepository;

    @KafkaListener(
            topics = "coupon-events-email",
            groupId = "notification-group",
            containerFactory = "KafkaListenerContainerFactory"
    )
    public void listenCouponEvent(
            String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack
    ) throws JsonProcessingException {
        log.debug("수신된 원본 메시지 - partition: {}, offset: {}, message: {}",
                partition, offset, message);
        try {
            CouponEvent event = parseAndValidateEvent(message);
            sendNotificationEmail(event);
            ack.acknowledge();
            log.info("이메일 발송 완료 및 offset commit = partition: {}, offset: {}, message: {}", partition, offset, message);
        }catch (Exception e){
            // 모든 예외를 DB에 저장
            saveFailedMessage(message, partition, offset, e);
            ack.acknowledge(); // DB에 저장했으므로 커밋
            log.error("메시지 처리 실패 - DB에 저장됨 - partition: {}, offset: {}",
                    partition, offset, e);
        }
    }


    private void saveFailedMessage(String message, int partition, long offset, Exception e) {
        FailMessage failedMessage = FailMessage.builder()
                .message(message)
                .partitionNumber(partition)
                .offset(offset)
                .errorMessage(e.getMessage())
                .errorType(e.getClass().getSimpleName())
                .failedAt(LocalDateTime.now())
                .retryCount(0)
                .status("FAILED")
                .build();

        failedMessageRepository.save(failedMessage);
    }

    private CouponEvent parseAndValidateEvent(String message) throws JsonProcessingException {
        CouponEvent event = objectMapper.readValue(message, CouponEvent.class);

        if (!isValidEmail(event.getEmail())) {
            throw new InvalidEmailException("유효하지 않은 이메일 주소: " + event.getEmail());
        }

        return event;
    }

    private boolean isValidEmail(String email) {
        return email != null &&
                !email.trim().isEmpty() &&
                email.matches("^[A-Za-z0-9+_.-]+@(.+)$");  // 간단한 이메일 형식 검증
    }

    private void sendNotificationEmail(CouponEvent event) throws MessagingException {
        String emailMessage = event.getNotificationMessage() != null ?
                event.getNotificationMessage() :
                "쿠폰이 발급되었습니다!";

        emailService.sendEmail(event.getEmail(), "쿠폰 발급 알림", emailMessage);
        log.info("이메일 발송 완료 - Email: {}", event.getEmail());
    }
}


