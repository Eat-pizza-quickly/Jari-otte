package com.eatpizzaquickly.couponservice.controller;



import com.eatpizzaquickly.couponservice.common.advice.ApiResponse;
import com.eatpizzaquickly.couponservice.dto.CouponRequestDto;
import com.eatpizzaquickly.couponservice.dto.CouponResponseDto;
import com.eatpizzaquickly.couponservice.entity.Coupon;
import com.eatpizzaquickly.couponservice.kafka.CouponEvent;
import com.eatpizzaquickly.couponservice.kafka.CouponEventProducer;
import com.eatpizzaquickly.couponservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final CouponEventProducer eventProducer;


    @PostMapping
    public ResponseEntity<ApiResponse<CouponResponseDto>> createCoupon(@RequestBody CouponRequestDto couponRequestDto) {
        CouponResponseDto coupon = couponService.createCoupon(couponRequestDto);
        return ResponseEntity.ok(ApiResponse.success("쿠폰발급 성공!",coupon));
    }

    @PostMapping("/{couponId}/issue-all")
    public ResponseEntity<ApiResponse<Void>> issueCoupon(@PathVariable("couponId") Long couponId) {
        couponService.issueCouponToAllUsers(couponId);
        return ResponseEntity.ok(ApiResponse.success("전체 사용자에게 쿠폰 발급이 요청되었습니다. 잠시 후 발급이 완료됩니다."));
    }
    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<Void>> issueCoupon(
            @RequestHeader("X-Authenticated-User") Long userId,
            @RequestBody CouponRequestDto couponRequestDto) {
        couponService.issueCouponToUser(userId,couponRequestDto.getCouponCode());

        return ResponseEntity.ok(ApiResponse.success("쿠폰 발급이 요청되었습니다. 잠시 후 발급이 완료됩니다."));
    }

    @GetMapping("/my-coupon")
    public ResponseEntity<ApiResponse<List<CouponResponseDto>>> getMyCoupons(@RequestHeader("X-Authenticated-User") Long userId) {
        List<CouponResponseDto> coupon = couponService.findAvailableCoupons(userId);
        return ResponseEntity.ok(ApiResponse.success("사용 가능한 쿠폰 조회 성공",coupon));
    }
}
