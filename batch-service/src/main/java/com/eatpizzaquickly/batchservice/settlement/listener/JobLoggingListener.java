package com.eatpizzaquickly.batchservice.settlement.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobLoggingListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Starting Job: {}", jobExecution.getJobInstance().getJobName());
        log.info("Job Parameters: {}", jobExecution.getJobParameters());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        ExitStatus exitStatus = jobExecution.getExitStatus();
        log.info("Completed Job: {}", jobExecution.getJobInstance().getJobName());
        log.info("Job Status: {}", jobExecution.getStatus());
        log.info("Job Exit Status: {}", exitStatus);

        // 실패한 경우 예외 메시지 로깅
        if (!exitStatus.equals(ExitStatus.COMPLETED)) {
            jobExecution.getAllFailureExceptions().forEach(ex ->
                    log.error("Job Exception: ", ex)
            );
        }
    }
}
