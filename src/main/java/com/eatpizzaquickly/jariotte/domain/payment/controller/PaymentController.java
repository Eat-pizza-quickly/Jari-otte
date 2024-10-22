package com.eatpizzaquickly.jariotte.domain.payment.controller;

import com.eatpizzaquickly.jariotte.domain.payment.dto.request.PostPaymentRequest;
import com.eatpizzaquickly.jariotte.domain.payment.dto.response.GetPaymentResponse;
import com.eatpizzaquickly.jariotte.domain.payment.dto.response.PostPaymentResponse;
import com.eatpizzaquickly.jariotte.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    /* 결제 요청 */
    @PostMapping("/toss")
    public ResponseEntity<PostPaymentResponse> requestTossPayment(
            @RequestBody PostPaymentRequest request
    ) {
        return ResponseEntity.ok(paymentService.requestTossPayment(request));
    }

    /* 결제 성공 처리 */
    @GetMapping("/toss/success")
    public ResponseEntity<GetPaymentResponse> TossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {
        return ResponseEntity.ok(paymentService.TossPaymentSuccess(paymentKey, orderId, amount));
    }

    /* 결제 실패 처리 */
    @GetMapping("/toss/fail")
    public ResponseEntity<GetPaymentResponse> tossPaymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId
    ) {
        return ResponseEntity.ok(paymentService.tossPaymentFail(code, message, orderId));
    }
}
