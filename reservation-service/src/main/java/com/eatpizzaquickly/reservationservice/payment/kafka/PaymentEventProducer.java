package com.eatpizzaquickly.reservationservice.payment.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {
    private final KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate;

    @Value("${spring.kafka.topic.payment-events}")
    private String topicName;

    public void sendPaymentSuccessEvent(Long paymentId, String email, Long amount) {
        PaymentEvent event = PaymentEvent.builder()
                .eventType("PAYMENT_SUCCESS")
                .paymentId(paymentId)
                .email(email)
                .amount(amount)
                .paymentStatus("SUCCESS")
                .notificationMessage("결제가 성공적으로 완료되었습니다.")
                .build();

        sendEvent(event);
    }

    public void sendPaymentCancelEvent(Long paymentId, String email, Long amount, String cancelReason) {
        PaymentEvent event = PaymentEvent.builder()
                .eventType("PAYMENT_CANCEL")
                .paymentId(paymentId)
                .email(email)
                .amount(amount)
                .paymentStatus("CANCELLED")
                .notificationMessage(String.format("결제가 취소되었습니다. 취소 사유: %s", cancelReason))
                .build();

        sendEvent(event);
    }

    private void sendEvent(PaymentEvent event) {
        paymentKafkaTemplate.send(topicName, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("결제 이벤트 전송 성공: {}, 오프셋: {}",
                                event, result.getRecordMetadata().offset());
                    } else {
                        log.error("결제 이벤트 전송 실패: {}", event, ex);
                    }
                });
    }
}