package com.eatpizzaquickly.batchservice.coupon.batch;

import com.eatpizzaquickly.batchservice.coupon.client.CouponServiceClient;
import com.eatpizzaquickly.batchservice.coupon.dto.UserCouponDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FeignUserCouponReader implements ItemReader<UserCouponDto> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private final CouponServiceClient couponServiceClient;
    private final int pageSize;
    private Iterator<UserCouponDto> currentItems;
    private boolean noMoreData = false;

    @Override
    public UserCouponDto read() {
        if (noMoreData) {
            return null;
        }

        try {
            // 현재 페이지의 데이터가 없거나 모두 처리된 경우
            if (currentItems == null || !currentItems.hasNext()) {
                Page<UserCouponDto> page = fetchFirstPage(); // 항상 첫 페이지를 가져옴

                if (!page.hasContent()) {
                    log.info("No more expired user coupons to process");
                    noMoreData = true;
                    return null;
                }

                currentItems = page.getContent().iterator();
                log.info("Initialized iterator with {} items from the first page", page.getContent().size());
            }

            // 현재 아이템 반환
            return currentItems.next();
        } catch (Exception e) {
            log.error("Error reading expired user coupons: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read expired user coupons", e);
        }
    }

    private Page<UserCouponDto> fetchFirstPage() {
        String currentDate = LocalDate.now().format(DATE_FORMATTER);
        log.info("Requesting expired user coupons - page: 0, size: {}, date: {}", pageSize, currentDate);

        try {
            Page<UserCouponDto> page = couponServiceClient.getExpiredUserCoupons(
                    currentDate,
                    0, // 항상 첫 페이지로 요청
                    pageSize
            );

            if (page.hasContent()) {
                log.info("Retrieved {} expired user coupons from the first page. First item: id={}, userId={}, expiryDate={}",
                        page.getContent().size(),
                        page.getContent().get(0).getId(),
                        page.getContent().get(0).getUserId(),
                        page.getContent().get(0).getExpiryDate());
            } else {
                log.info("No expired user coupons found on the first page");
            }

            return page;
        } catch (Exception e) {
            log.error("Failed to fetch the first page of expired user coupons: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch expired user coupons", e);
        }
    }
}