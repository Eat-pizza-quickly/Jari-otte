package com.eatpizzaquickly.couponservice.controller;

import com.eatpizzaquickly.couponservice.dto.CouponDto;
import com.eatpizzaquickly.couponservice.dto.UserCouponDto;
import com.eatpizzaquickly.couponservice.repository.UserCouponRepository;
import com.eatpizzaquickly.couponservice.service.CouponBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CouponBatchController {

    private final CouponBatchService couponBatchService;
    private final UserCouponRepository userCouponRepository;

    @GetMapping("/coupons/expired")
    public ResponseEntity<List<CouponDto>> getExpiredCoupons(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1500") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(couponBatchService.findExpiredCoupons(currentDate, pageRequest));
    }

    @PutMapping("/coupons/deactivate")
    public ResponseEntity<Void> deactivateExpiredCoupons(@RequestBody List<Long> couponIds) {
        couponBatchService.deactivateExpiredCoupons(couponIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user-coupons/expired")
    public ResponseEntity<Page<UserCouponDto>> getExpiredUserCoupons(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "500") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(couponBatchService.findExpiredUserCoupons(currentDate, pageRequest));
    }

    @DeleteMapping("/user-coupons")
    public ResponseEntity<Void> deleteExpiredUserCoupons(@RequestBody List<Long> userCouponIds) {
        couponBatchService.deleteExpiredUserCoupons(userCouponIds);
        return ResponseEntity.ok().build();
    }
}