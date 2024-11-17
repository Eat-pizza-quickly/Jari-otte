package com.eatpizzaquickly.batchservice.coupon.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class CouponBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job couponBatchJob;

    //@Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Seoul")
    public void runBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(couponBatchJob, jobParameters);
            log.info("Coupon batch job completed successfully");
        } catch (Exception e) {
            log.error("Error occurred while running coupon batch job", e);
        }
    }
}
