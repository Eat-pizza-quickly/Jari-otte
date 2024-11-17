package com.eatpizzaquickly.batchservice.coupon.batch;

import com.eatpizzaquickly.batchservice.coupon.client.CouponServiceClient;
import com.eatpizzaquickly.batchservice.coupon.dto.CouponDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;


import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FeignCouponReader implements ItemReader<CouponDto> {
    private final CouponServiceClient couponServiceClient;
    private final int pageSize;
    private int currentPage = 0;
    private Iterator<CouponDto> currentItems;
    private boolean noMoreData = false;
    private final String currentDate;

    public FeignCouponReader(CouponServiceClient couponServiceClient, int pageSize) {
        this.couponServiceClient = couponServiceClient;
        this.pageSize = pageSize;
        this.currentDate = LocalDate.now().toString();
    }

    @Override
    public CouponDto read() {
        try {
            if (noMoreData) {
                return null;
            }

            if (currentItems == null || !currentItems.hasNext()) {
                log.info("Requesting expired coupons - currentDate: {}, page: {}, size: {}",
                        currentDate, currentPage, pageSize);

                List<CouponDto> items = couponServiceClient.getExpiredCoupons(
                        currentDate,
                        currentPage,
                        pageSize
                );
                log.info("Retrieved {} expired coupons", items.size());

                if (items.isEmpty()) {
                    log.info("No more expired coupons to process");
                    noMoreData = true;
                    return null;
                }

                currentItems = items.iterator();
                currentPage++;
            }

            return currentItems.next();
        } catch (Exception e) {
            log.error("Error reading expired coupons: {}", e.getMessage(), e);
            throw e;
        }
    }
}