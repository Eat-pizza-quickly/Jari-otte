package com.eatpizzaquickly.jariotte.domain.payment.service;

import com.eatpizzaquickly.jariotte.domain.common.config.TossPaymentConfig;
import com.eatpizzaquickly.jariotte.domain.payment.dto.response.TossPaymentResponse;
import com.eatpizzaquickly.jariotte.domain.payment.dto.request.PaymentConfirmRequest;
import com.eatpizzaquickly.jariotte.domain.payment.dto.request.PostPaymentRequest;
import com.eatpizzaquickly.jariotte.domain.payment.dto.response.GetPaymentResponse;
import com.eatpizzaquickly.jariotte.domain.payment.dto.response.PostPaymentResponse;
import com.eatpizzaquickly.jariotte.domain.payment.entity.PayMethod;
import com.eatpizzaquickly.jariotte.domain.payment.entity.PayStatus;
import com.eatpizzaquickly.jariotte.domain.payment.entity.Payment;
import com.eatpizzaquickly.jariotte.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final TossPaymentConfig tossPaymentConfig;
    private final RestTemplate restTemplate;

    /* 결제 요청 */
    public PostPaymentResponse requestTossPayment(PostPaymentRequest request) {
        String orderId = UUID.randomUUID().toString();
        Payment payment = new Payment(
                orderId,
                request.getAmount(),
                request.getPayInfo(),
                PayMethod.TOSS,
                PayStatus.READY
        );
        return new PostPaymentResponse(paymentRepository.save(payment), tossPaymentConfig.getSuccessUrl(), tossPaymentConfig.getFailureUrl());
    }

    /* 결제 성공 */
    @Transactional
    public GetPaymentResponse TossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        Payment payment = paymentRepository.findByPayUid(orderId).orElseThrow(
                () -> new IllegalArgumentException("결제를 찾을 수 없습니다."));

        if (payment.getAmount() != amount) {
            throw new IllegalArgumentException("금액이 일치 하지 않습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((tossPaymentConfig.getTestClientSecretKey() + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        PaymentConfirmRequest confirmRequest = new PaymentConfirmRequest(paymentKey, orderId, amount);

        HttpEntity<PaymentConfirmRequest> request = new HttpEntity<>(confirmRequest, headers);

        try {
            ResponseEntity<TossPaymentResponse> response = restTemplate.postForEntity(
                    "https://api.tosspayments.com/v1/payments/confirm",
                    request,
                    TossPaymentResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                payment.setPayStatus(PayStatus.PAID);
                paymentRepository.save(payment);

                return GetPaymentResponse.builder()
                        .payStatus(PayStatus.PAID)
                        .paymentKey(response.getBody().getPaymentKey())
                        .amount(response.getBody().getTotalAmount())
                        .build();
            }
        } catch (RuntimeException e) {
                payment.setPayStatus(PayStatus.FAILED);
                paymentRepository.save(payment);
                throw new RuntimeException("Payment failed: " + e.getMessage());
        }
        throw new RuntimeException("Payment failed");
    }

    public GetPaymentResponse tossPaymentFail(String code, String message, String orderId) {
        Payment payment = paymentRepository.findByPayUid(orderId)
                .orElseThrow(() -> new RuntimeException("결제를 찾을 수 없습니다."));

        payment.setPayStatus(PayStatus.FAILED);
        paymentRepository.save(payment);

        return GetPaymentResponse.builder()
                .payStatus(PayStatus.FAILED)
                .message(message)
                .code(code)
                .build();
    }
}
