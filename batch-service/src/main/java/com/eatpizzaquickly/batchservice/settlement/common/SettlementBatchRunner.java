package com.eatpizzaquickly.batchservice.settlement.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class SettlementBatchRunner {
    private final JobLauncher jobLauncher;
    private final Job settlementBatchJob;

    public void runSettlementBatchJob() {
        try {
            jobLauncher.run(settlementBatchJob, new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());
            log.info("Settlement batch job completed successfully");
        } catch (Exception e) {
            log.error("Error occurred while running batch job", e);
        }
    }
}