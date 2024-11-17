package com.eatpizzaquickly.batchservice.settlement.config;

import com.eatpizzaquickly.batchservice.settlement.listener.JobLoggingListener;
import com.eatpizzaquickly.batchservice.settlement.listener.StepLoggingListener;
import com.eatpizzaquickly.batchservice.settlement.dto.request.HostPointRequestDto;
import com.eatpizzaquickly.batchservice.settlement.dto.request.PaymentRequestDto;
import com.eatpizzaquickly.batchservice.settlement.dto.response.PaymentResponseDto;
import com.eatpizzaquickly.batchservice.settlement.entity.HostPoint;
import com.eatpizzaquickly.batchservice.settlement.entity.TempPayment;
import com.eatpizzaquickly.batchservice.settlement.processor.HostPointProcessor;
import com.eatpizzaquickly.batchservice.settlement.processor.PaymentProcessor;
import com.eatpizzaquickly.batchservice.settlement.reader.HostPointReader;
import com.eatpizzaquickly.batchservice.settlement.reader.PaymentReader;
import com.eatpizzaquickly.batchservice.settlement.tasklet.TaskletConfig;
import com.eatpizzaquickly.batchservice.settlement.writer.HostPointWriter;
import com.eatpizzaquickly.batchservice.settlement.writer.PaymentWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.eatpizzaquickly.batchservice.settlement.common.BatchConstant.CHUNK_SIZE;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class SettlementBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final HostPointProcessor hostPointProcessor;
    private final PaymentProcessor paymentProcessor;
    private final HostPointReader hostPointReader;
    private final PaymentReader paymentReader;
    private final HostPointWriter hostPointWriter;
    private final PaymentWriter paymentWriter;
    private final TaskletConfig taskletConfig;
    private final JobLoggingListener jobLoggingListener;
    private final StepLoggingListener stepLoggingListener;


    @Bean
    public Job settlementBatchJob() {
        return new JobBuilder("SettlementBatchJob", jobRepository)
                .listener(jobLoggingListener)
                .start(settlementStep())
                .next(hostPointStorageStep())
                .next(pointTransmissionStep())
                .next(updatePaymentWithTestPaymentStep())
                .next(deleteTempTable())
                .build();
    }

    public Step settlementStep() {
        return new StepBuilder("settlementStep", jobRepository)
                .<PaymentResponseDto, TempPayment>chunk(CHUNK_SIZE, transactionManager)
                .reader(paymentReader.paidPaymentReader())
                .processor(paymentProcessor.paymentSettleProcessor())
                .writer(paymentWriter.tempPaymentWriter())
                .listener(stepLoggingListener)
                .build();
    }

    public Step hostPointStorageStep() {
        return new StepBuilder("hostPointStorageStep", jobRepository)
                .<TempPayment, HostPoint>chunk(CHUNK_SIZE, transactionManager)
                .reader(paymentReader.pointAdditionReader())
                .processor(paymentProcessor.pointAdditionProcessor())
                .writer(hostPointWriter.hostPointWriter())
                .listener(stepLoggingListener)
                .build();
    }

    public Step pointTransmissionStep() {
        return new StepBuilder("pointTransmissionStep", jobRepository)
                .<HostPoint, HostPointRequestDto>chunk(CHUNK_SIZE, transactionManager)
                .reader(hostPointReader.hostPointReader())
                .processor(hostPointProcessor.pointTransmissionProcessor())
                .writer(hostPointWriter.hostPointTransmissionWriter())
                .listener(stepLoggingListener)
                .build();
    }

    public Step updatePaymentWithTestPaymentStep() {
        return new StepBuilder("updatePaymentWithTestPaymentStep", jobRepository)
                .<TempPayment, PaymentRequestDto>chunk(CHUNK_SIZE, transactionManager)
                .reader(paymentReader.updatePaymentReader())
                .processor(paymentProcessor.updatePaymentProcessor())
                .writer(paymentWriter.paymentWriter())
                .listener(stepLoggingListener)
                .build();
    }

    public Step deleteTempTable() {
        return new StepBuilder("deleteTempTable()", jobRepository)
                .tasklet(taskletConfig.deleteTempTableTasklet(), transactionManager)
                .build();
    }

}
