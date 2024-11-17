package com.eatpizzaquickly.batchservice.coupon.batch;

import com.eatpizzaquickly.batchservice.coupon.client.CouponServiceClient;
import com.eatpizzaquickly.batchservice.coupon.dto.CouponDto;
import com.eatpizzaquickly.batchservice.coupon.dto.UserCouponDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.List;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CouponBatchConfig {

    private static final int CHUNK_SIZE = 500;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CouponServiceClient couponServiceClient;

    @Bean
    public Job couponBatchJob() {
        log.info("Initializing couponBatchJob");
        return new JobBuilder("couponBatchJob", jobRepository)
                .start(deleteExpiredUserCouponsStep())
                .next(deactivateExpiredCouponsStep())
                .build();
    }

    @Bean
    public ItemReader<UserCouponDto> userCouponReader() {
        log.info("Creating FeignUserCouponReader");
        return new FeignUserCouponReader(couponServiceClient, CHUNK_SIZE);
    }

    @Bean
    public ItemReader<? extends CouponDto> couponReader() {
        log.info("Creating FeignCouponReader");
        return new FeignCouponReader(couponServiceClient, CHUNK_SIZE);
    }

    @Bean
    public Step deleteExpiredUserCouponsStep() {
        log.info("Initializing deleteExpiredUserCouponsStep");
        return new StepBuilder("deleteExpiredUserCouponsStep", jobRepository)
                .<UserCouponDto, UserCouponDto>chunk(CHUNK_SIZE, transactionManager)
                .reader(userCouponReader())
                .processor(item -> {
                    log.info("Processing expired user coupon - userId: {}, couponId: {}",
                            item.getUserId(), item.getCouponId());
                    return item;
                })
                .writer(createUserCouponWriter())
                .build();
    }

    @Bean
    public Step deactivateExpiredCouponsStep() {
        log.info("Initializing deactivateExpiredCouponsStep");
        return new StepBuilder("deactivateExpiredCouponsStep", jobRepository)
                .<CouponDto, CouponDto>chunk(CHUNK_SIZE, transactionManager)
                .reader(couponReader())
                .processor(item -> {
                    log.info("Processing expired coupon - couponId: {}, code: {}",
                            item.getId(), item.getCouponCode());
                    return item;
                })
                .writer(createCouponWriter())
                .build();
    }

    private ItemWriter<UserCouponDto> createUserCouponWriter() {
        return chunk -> {
            List<Long> userCouponIds = chunk.getItems().stream()
                    .map(UserCouponDto::getId)
                    .toList();

            if (!userCouponIds.isEmpty()) {
                couponServiceClient.deleteExpiredUserCoupons(userCouponIds);
                log.info("Deleted {} expired user coupons", userCouponIds);
            }
        };
    }

    private ItemWriter<CouponDto> createCouponWriter() {
        return chunk -> {
            List<Long> couponIds = chunk.getItems().stream()
                    .map(CouponDto::getId)
                    .toList();

            if (!couponIds.isEmpty()) {
                couponServiceClient.deactivateExpiredCoupons(couponIds);
                log.info("Deactivated {} expired coupons", couponIds.size());
            }
        };
    }
}