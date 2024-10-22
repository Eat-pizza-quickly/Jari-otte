package com.eatpizzaquickly.jariotte.domain.coupon.controller;

import com.eatpizzaquickly.jariotte.domain.common.advice.ApiResponse;
import com.eatpizzaquickly.jariotte.domain.common.dto.CustomUserDetails;
import com.eatpizzaquickly.jariotte.domain.coupon.dto.CouponRequestDto;
import com.eatpizzaquickly.jariotte.domain.coupon.dto.CouponResponseDto;
import com.eatpizzaquickly.jariotte.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CouponResponseDto>> createCoupon(@RequestBody CouponRequestDto couponRequestDto) {
        CouponResponseDto coupon = couponService.createCoupon(couponRequestDto);
        return ResponseEntity.ok(ApiResponse.success("쿠폰발급 성공!",coupon));
    }

    @PostMapping("/{couponId}/issue-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> issueCoupon(@PathVariable Long couponId) {
        couponService.issueCouponToAllUsers(couponId);
        return ResponseEntity.ok(ApiResponse.success("전체 사용자에게 쿠폰이 발급이 되었습니다!"));
    }
    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<Void>> issueCoupon(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @RequestBody CouponRequestDto couponRequestDto) {
        couponService.issueCouponToUser(authUser.getEmail(),couponRequestDto.getCouponCode());
        return ResponseEntity.ok(ApiResponse.success("쿠폰이 성공적으로 발급이 완료 되었습니다."));
    }



}
