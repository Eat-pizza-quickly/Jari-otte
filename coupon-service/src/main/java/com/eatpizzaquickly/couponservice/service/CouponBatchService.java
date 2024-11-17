package com.eatpizzaquickly.couponservice.service;

import com.eatpizzaquickly.couponservice.dto.CouponDto;
import com.eatpizzaquickly.couponservice.dto.UserCouponDto;
import com.eatpizzaquickly.couponservice.entity.Coupon;
import com.eatpizzaquickly.couponservice.entity.UserCoupon;
import com.eatpizzaquickly.couponservice.repository.CouponsRepository;
import com.eatpizzaquickly.couponservice.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponBatchService {

    private final CouponsRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional(readOnly = true)
    public List<CouponDto> findExpiredCoupons(LocalDate currentDate, Pageable pageable) {
        log.info("Finding expired coupons for date: {}, page: {}, size: {}",
                currentDate, pageable.getPageNumber(), pageable.getPageSize());

        List<CouponDto> expiredCoupons = couponRepository.findExpiredCoupons(currentDate, pageable)
                .stream()
                .map(this::convertToCouponDto)
                .collect(Collectors.toList());

        log.info("Found {} expired coupons", expiredCoupons.size());
        return expiredCoupons;
    }

    @Transactional
    public void deactivateExpiredCoupons(List<Long> couponIds) {
        if (couponIds.isEmpty()) {
            return;
        }
        LocalDate currentDate = LocalDate.now();
        // 벌크 업데이트 수행
        int updatedCount = couponRepository.deactivateExpiredCoupons(couponIds, currentDate);
        log.info("Deactivated {} expired coupons", updatedCount);
    }

    @Transactional(readOnly = true)
    public Page<UserCouponDto> findExpiredUserCoupons(LocalDate currentDate, Pageable pageable) {
        return userCouponRepository.findExpiredUserCoupons(currentDate, pageable)
                .map(this::convertToUserCouponDto);
    }

    @Transactional
    public void deleteExpiredUserCoupons(List<Long> userCouponIds) {
        if (userCouponIds.isEmpty()) {
            return;
        }
        LocalDate currentDate = LocalDate.now();
        // 2. 지정된 ID의 만료된 쿠폰 삭제
        int deletedCount = userCouponRepository.deleteExpiredUserCoupons(userCouponIds);
        log.info("Deleted {} expired user coupons", deletedCount);
    }

    private CouponDto convertToCouponDto(Coupon coupon) {
        return CouponDto.builder()
                .id(coupon.getId())
                .couponCode(coupon.getCouponCode())
                .expiryDate(coupon.getExpiryDate())
                .isActive(coupon.isCouponActive())
                .build();
    }

    private UserCouponDto convertToUserCouponDto(UserCoupon userCoupon) {
        return UserCouponDto.builder()
                .id(userCoupon.getId())
                .userId(userCoupon.getUserId())
                .couponId(userCoupon.getCouponId())
                .expiryDate(userCoupon.getExpiryDate())
                .build();
    }
}