package com.eatpizzaquickly.reservationservice.payment.service;

import com.eatpizzaquickly.reservationservice.common.config.TossPaymentConfig;
import com.eatpizzaquickly.reservationservice.common.exception.NotFoundException;
import com.eatpizzaquickly.reservationservice.payment.client.CouponFeignClient;
import com.eatpizzaquickly.reservationservice.payment.client.UserClient;
import com.eatpizzaquickly.reservationservice.payment.dto.PaymentRequestDto;
import com.eatpizzaquickly.reservationservice.payment.dto.request.PaymentConfirmRequest;
import com.eatpizzaquickly.reservationservice.payment.dto.request.PostPaymentRequest;
import com.eatpizzaquickly.reservationservice.payment.dto.response.GetPaymentResponse;
import com.eatpizzaquickly.reservationservice.payment.dto.response.PaymentResponseDto;
import com.eatpizzaquickly.reservationservice.payment.dto.response.TossPaymentResponse;
import com.eatpizzaquickly.reservationservice.payment.dto.response.UserResponseDto;
import com.eatpizzaquickly.reservationservice.payment.entity.PayMethod;
import com.eatpizzaquickly.reservationservice.payment.entity.PayStatus;
import com.eatpizzaquickly.reservationservice.payment.entity.Payment;
import com.eatpizzaquickly.reservationservice.payment.entity.SettlementStatus;
import com.eatpizzaquickly.reservationservice.payment.exception.*;
import com.eatpizzaquickly.reservationservice.payment.kafka.PaymentEventProducer;
import com.eatpizzaquickly.reservationservice.payment.repository.PaymentRepository;
import com.eatpizzaquickly.reservationservice.reservation.entity.Reservation;
import com.eatpizzaquickly.reservationservice.reservation.entity.ReservationStatus;
import com.eatpizzaquickly.reservationservice.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final TossPaymentConfig tossPaymentConfig;
    private final RestTemplate restTemplate;
    private final CouponFeignClient couponFeignClient;
    private final UserClient userClient;
    private final PaymentEventProducer paymentEventProducer; // 결제 이벤트 프로듀서 주입


    @Value("${payment.toss.url}")
    private String TOSS_URL;

    @Value("${payment.toss.test_secret_api_key}")
    private String TOSS_SECRET_KEY;

    @Value("${payment.toss.success_url}")
    private String SUCCESS_URL;

    @Value("${payment.toss.fail_url}")
    private String FAIL_URL;

    /* 결제 요청 */
    public String requestTossPayment(PostPaymentRequest request, Long couponId) {
        // 예약 확인
        Reservation reservation = reservationRepository.findById(request.getReservationId()).orElseThrow(
                () -> new NotFoundException("예약을 찾을수 없습니다."));

        // 주문 ID 생성
        String orderId = UUID.randomUUID().toString();
        Long price = request.getAmount();
        //feignclient 호출로 수정
        if (couponId != null) {
            price = couponFeignClient.applyCoupon(couponId, request.getAmount()); // 쿠폰 적용 값
        }
        // DB 저장
        Payment payment = new Payment(
                orderId,
                price,
                request.getPayInfo(),
                PayMethod.TOSS,
                PayStatus.READY,
                reservation
        );
        paymentRepository.save(payment);

        // 토스 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((TOSS_SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("amount", price);
        payloadMap.put("orderId", orderId);
        payloadMap.put("orderName", request.getPayInfo());
        payloadMap.put("method", "카드");
        payloadMap.put("successUrl", SUCCESS_URL);
        payloadMap.put("failUrl", FAIL_URL);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payloadMap, headers);

        try {
            ResponseEntity<TossPaymentResponse> response = new RestTemplate().exchange(
                    TOSS_URL,
                    HttpMethod.POST,
                    requestEntity,
                    TossPaymentResponse.class
            );

            if (response.getBody() != null) {
                String paymentKey = response.getBody().getPaymentKey();
                // 리다이렉트 URL 생성
                return SUCCESS_URL + "?orderId=" + orderId
                        + "&paymentKey=" + paymentKey
                        + "&amount=" + price;
            }
        } catch (Exception e) {
            log.error("토스 결제 요청 실패: ", e);
            throw new PaymentException("결제 요청 중 오류가 발생했습니다.");
        }

        return null;
    }

    @Transactional
    public GetPaymentResponse TossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        // 1. 결제 정보 조회
        Payment payment = paymentRepository.findByPayUid(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("결제 정보를 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(payment.getReservation().getId()).orElseThrow(
                () -> new NotFoundException("예약을 찾을수 없습니다."));

        try {
            // 2. 토스페이먼츠 결제 승인 API 호출
            TossPaymentResponse tossResponse = requestTossPayment(paymentKey, orderId, amount);

            // 3. 결제 성공 처리
            payment.setPayStatus(PayStatus.PAID);
            payment.setPaymentKey(tossResponse.getPaymentKey());
            payment.setPaidAt(LocalDateTime.now());
            paymentRepository.save(payment);

            // 예약 상태 업데이트
            reservation.statusUpdate(ReservationStatus.CONFIRMED);

            // 4. 주문 처리 로직 (필요한 경우)
            // orderService.completeOrder(orderId);
            UserResponseDto user = userClient.getUserById(reservation.getUserId()).getData();
            String userEmail = user.getEmail();

            paymentEventProducer.sendPaymentSuccessEvent(
                    payment.getId(),
                    userEmail,  // 가져온 이메일을 전달
                    payment.getAmount()
            );

            return GetPaymentResponse.builder()
                    .payStatus(PayStatus.PAID)
                    .paymentKey(tossResponse.getPaymentKey())
                    .amount(tossResponse.getTotalAmount())
                    .build();

        } catch (HttpClientErrorException e) {
            // 5. HTTP 에러 처리
            payment.setPayStatus(PayStatus.FAILED);
            paymentRepository.save(payment);

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // 세션 만료시
                throw new PaymentSessionExpiredException("결제 세션이 만료되었습니다. 다시 결제를 시도해주세요.");
            }
            // 기타 에러
            throw new PaymentProcessingException("결제 처리 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            // 6. 기타 예외 처리
            payment.setPayStatus(PayStatus.FAILED);
            paymentRepository.save(payment);
            throw new PaymentProcessingException("결제 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
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

    @Transactional
    public GetPaymentResponse cancelPayment(String paymentKey, String cancelReason) {
        // 1. 결제 정보 조회
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new PaymentNotFoundException("결제 정보를 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(payment.getReservation().getId()).orElseThrow(
                () -> new NotFoundException("예약을 찾을수 없습니다."));

        // 2. 결제 상태 확인
        if (payment.getPayStatus() == PayStatus.CANCELLED) {
            throw new PaymentAlreadyCanceledException("이미 취소된 결제입니다.");
        }

        if (payment.getPayStatus() != PayStatus.PAID) {
            throw new PaymentCancelException("결제 완료 상태인 경우에만 취소가 가능합니다.");
        }

        try {
            // 3. 토스페이먼츠 결제 취소 API 호출
            TossPaymentResponse tossResponse = requestTossPaymentCancel(paymentKey, cancelReason);

            // 4. 결제 취소 상태 업데이트
            payment.setPayStatus(PayStatus.CANCELLED);
            paymentRepository.save(payment);

            // 예약 상태 업데이트
            reservation.statusUpdate(ReservationStatus.CANCELED);

            // 결제 취소 이벤트 발행
            UserResponseDto user = userClient.getUserById(reservation.getUserId()).getData();
            String userEmail = user.getEmail();

            paymentEventProducer.sendPaymentCancelEvent(
                    payment.getId(),
                    userEmail,
                    payment.getAmount(),
                    cancelReason
            );

            return GetPaymentResponse.builder()
                    .payStatus(PayStatus.CANCELLED)
                    .paymentKey(payment.getPaymentKey())
                    .amount(payment.getAmount())
                    .payInfo(payment.getPayUid())
                    .message("결제가 성공적으로 취소되었습니다.")
                    .build();
        } catch (HttpClientErrorException e) {
            log.error("결제 취소 실패: {}", e.getMessage());
            throw new PaymentCancelException("결제 취소 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 토스페이먼츠 API 호출 메서드 분리
    private TossPaymentResponse requestTossPayment(String paymentKey, String orderId, Long amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((tossPaymentConfig.getTestClientSecretKey() + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        PaymentConfirmRequest confirmRequest = new PaymentConfirmRequest(paymentKey, orderId, amount);

        HttpEntity<PaymentConfirmRequest> request = new HttpEntity<>(confirmRequest, headers);

        ResponseEntity<TossPaymentResponse> response = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/confirm",
                request,
                TossPaymentResponse.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new PaymentProcessingException("토스페이먼츠 API 호출 실패");
        }

        return response.getBody();
    }

    private TossPaymentResponse requestTossPaymentCancel(String paymentKey, String cancelReason) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((TOSS_SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("cancelReason", cancelReason);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payloadMap, headers);

        ResponseEntity<TossPaymentResponse> response = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel",
                request,
                TossPaymentResponse.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new PaymentCancelException("토스페이먼츠 취소 API 호출 실패");
        }

        return response.getBody();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByStatus(SettlementStatus settlementStatus, PayStatus payStatus, int chunk, int currentOffset) {
        // 7일
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        Page<PaymentResponseDto> payments = paymentRepository.getPaymentsByStatus(settlementStatus, payStatus, sevenDaysAgo, currentOffset, chunk);
        return payments.getContent().stream()
                .toList();
    }

    @Transactional
    public void updatePayments(List<PaymentRequestDto> payments) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Payment> paymentList = payments.stream()
                .map(paymentRequestDto -> {
                    Payment payment = paymentRepository.findById(paymentRequestDto.getId())
                            .orElseThrow(() -> new PaymentNotFoundException("결제 내역이 없습니다."));
                    payment.setSettlementStatus(paymentRequestDto.getSettlementStatus());
                    payment.setSettledAt(currentTime);
                    return payment;
                }).toList();

        paymentRepository.saveAll(paymentList);
    }
}