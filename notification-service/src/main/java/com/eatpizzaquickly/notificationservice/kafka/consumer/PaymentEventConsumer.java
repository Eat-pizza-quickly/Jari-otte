package com.eatpizzaquickly.notificationservice.kafka.consumer;

import com.eatpizzaquickly.notificationservice.entity.FailMessage;
import com.eatpizzaquickly.notificationservice.exception.BusinessException;
import com.eatpizzaquickly.notificationservice.exception.MessageParsingException;
import com.eatpizzaquickly.notificationservice.kafka.dto.PaymentEvent;
import com.eatpizzaquickly.notificationservice.repository.FailedMessageRepository;
import com.eatpizzaquickly.notificationservice.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final FailedMessageRepository failedMessageRepository;


    @KafkaListener(
            topics = "payment-events",
            groupId = "notification-group",
            containerFactory = "KafkaListenerContainerFactory"
    )
    public void listenPaymentEvent(
            String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack
    ) throws JsonProcessingException {
        log.debug("수신된 원본 메시지 - partition: {}, offset: {}, message: {}",
                partition, offset, message);

        try {
            PaymentEvent event = parseAndValidateEvent(message);
            processEvent(event);
            ack.acknowledge();
            log.info("결제 알림 처리 완료 및 offset commit - partition: {}, offset: {}, PaymentID: {}",
                    partition, offset, event.getPaymentId());

        } catch (Exception e) {
            // 모든 예외를 DB에 저장
            saveFailedMessage(message, partition, offset, e);
            ack.acknowledge(); // DB에 저장했으므로 커밋
            log.error("메시지 처리 실패 - DB에 저장됨 - partition: {}, offset: {}",
                    partition, offset, e);
        }
    }

    private PaymentEvent parseAndValidateEvent(String message) throws MessageParsingException {
        try {
            PaymentEvent event = objectMapper.readValue(message, PaymentEvent.class);
            validateEvent(event);
            return event;
        } catch (JsonProcessingException e) {
            throw new MessageParsingException("JSON 파싱 실패: " + e.getMessage());
        }
    }

    private void validateEvent(PaymentEvent event) throws MessageParsingException {
        if (event.getEmail() == null || event.getEmail().trim().isEmpty()) {
            throw new MessageParsingException("유효하지 않은 이메일 주소");
        }
        if (event.getPaymentId() <= 0) {
            throw new MessageParsingException("유효하지 않은 결제 ID");
        }
        if (event.getAmount() < 0) {
            throw new MessageParsingException("유효하지 않은 결제 금액");
        }
    }

    private void processEvent(PaymentEvent event) throws BusinessException {
        try {
            log.info("결제 이벤트 처리 시작 - PaymentID: {}, Email: {}, Amount: {}",
                    event.getPaymentId(),
                    event.getEmail(),
                    event.getAmount());

            String emailMessage = createEmailMessage(event);
            emailService.sendEmail(event.getEmail(), "결제 알림", emailMessage);

            log.info("결제 알림 이메일 발송 완료 - PaymentID: {}", event.getPaymentId());
        } catch (MessagingException e) {
            throw new BusinessException("이메일 발송 실패: " + e.getMessage());
        }
    }

    private String createEmailMessage(PaymentEvent event) {
        return event.getNotificationMessage() != null ?
                event.getNotificationMessage() :
                String.format(
                        "결제가 %s 되었습니다.\n결제 ID: %d\n결제 금액: %d원",
                        event.getPaymentStatus(),
                        event.getPaymentId(),
                        event.getAmount()
                );
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
}