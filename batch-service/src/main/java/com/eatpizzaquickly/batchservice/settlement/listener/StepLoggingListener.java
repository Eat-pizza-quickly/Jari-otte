package com.eatpizzaquickly.batchservice.settlement.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StepLoggingListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("step start {}", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step 종료: {} ,읽은 아이템 수: {} ,처리된 아이템 수: {}",
                stepExecution.getStepName(),
                stepExecution.getReadCount(),
                stepExecution.getWriteCount());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Step 종료 상태: {}", stepExecution.getExitStatus());
        return stepExecution.getExitStatus();
    }
}
