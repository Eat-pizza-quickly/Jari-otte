package com.eatpizzaquickly.batchservice.settlement.reader;

import com.eatpizzaquickly.batchservice.common.client.PaymentClient;
import com.eatpizzaquickly.batchservice.settlement.dto.response.PaymentResponseDto;
import com.eatpizzaquickly.batchservice.settlement.entity.PayStatus;
import com.eatpizzaquickly.batchservice.settlement.entity.SettlementStatus;
import com.eatpizzaquickly.batchservice.settlement.entity.TempPayment;
import com.eatpizzaquickly.batchservice.settlement.repository.TempPaymentRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.eatpizzaquickly.batchservice.settlement.common.BatchConstant.CHUNK_SIZE;

@RequiredArgsConstructor
@Component
@Slf4j
public class PaymentReader {

    private final PaymentClient paymentClient;
    private final TempPaymentRepository tempPaymentRepository;
    private final EntityManagerFactory entityManagerFactory;
    private static final String OFFSET_KEY = "current_offset";
    private final RedisTemplate<String, String> redisTemplate;

    public ItemReader<PaymentResponseDto> paidPaymentReader() {
        return new ItemReader<PaymentResponseDto>() {
            private List<PaymentResponseDto> currentChunk = new ArrayList<>();

            @BeforeStep
            public void initializeOffset(StepExecution stepExecution) {
                // 배치 작업 시작 시, Redis에 키가 없으면 초기값 설정
                Boolean hasKey = redisTemplate.hasKey(OFFSET_KEY);
                if (Boolean.FALSE.equals(hasKey)) {
                    redisTemplate.opsForValue().set(OFFSET_KEY, "1");
                    log.info("Offset Initialize");
                }
            }

            @Override
            public PaymentResponseDto read() {
                // currentChunk가 비었을 경우 새로운 데이터를 가져옴
                if (currentChunk.isEmpty()) {
                    int currentOffset = Math.toIntExact(getAndIncrementOffset(CHUNK_SIZE)); //Long to int
                    log.info("현재 작업 Offset {}", currentOffset);

                    // ID를 기준으로 하는 ZeroOffsetReader로 변경
                    currentChunk = paymentClient.getPaymentsByStatusAfterId(
                            SettlementStatus.UNSETTLED, PayStatus.PAID, CHUNK_SIZE, currentOffset);

                    // 데이터가 없으면 null 반환
                    if (currentChunk.isEmpty()) {
                        log.info("Offset {} 데이터 처리 완료", currentOffset);
                        return null;
                    }
                }

                // 첫 번째 데이터를 반환하고, 리스트에서 제거
                PaymentResponseDto paymentResponseDto = currentChunk.remove(0);
                log.info("Processing PaymentResponseDto: {}", paymentResponseDto);
                return paymentResponseDto;
            }

            private Long getAndIncrementOffset(int increment) {
                // Redis에서 offset 증가 후 진행할 Offset 반환
                return redisTemplate.opsForValue().increment(OFFSET_KEY, CHUNK_SIZE) - CHUNK_SIZE;
            }
        };
    }


    @Bean
    public JpaPagingItemReader<TempPayment> pointAdditionReader() {
        return createReader("pointAdditionReader", SettlementStatus.PROGRESS);
    }

    @Bean
    public JpaPagingItemReader<TempPayment> updatePaymentReader() {
        return createReader("updatePaymentReader", SettlementStatus.SETTLED);
    }

    @Bean
    public JpaPagingItemReader<TempPayment> settlementSettledReader() {
        return createReader("settlementSettledReader", SettlementStatus.PROGRESS);
    }

    private JpaPagingItemReader<TempPayment> createReader(String name, SettlementStatus status) {
        return new JpaPagingItemReaderBuilder<TempPayment>()
                .name(name)
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("SELECT t FROM TempPayment t WHERE t.settlementStatus = :status ORDER BY t.id ASC")
                .parameterValues(Collections.singletonMap("status", status))
                .build();
    }


}
