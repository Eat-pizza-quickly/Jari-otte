package com.eatpizzaquickly.batchservice.settlement.reader;


import com.eatpizzaquickly.batchservice.settlement.entity.HostPoint;
import com.eatpizzaquickly.batchservice.settlement.repository.HostPointRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.stereotype.Component;

import static com.eatpizzaquickly.batchservice.settlement.common.BatchConstant.CHUNK_SIZE;

@RequiredArgsConstructor
@Component
@Slf4j
public class HostPointReader {
    private final HostPointRepository hostPointRepository;
    private final EntityManagerFactory entityManagerFactory;

    public JpaPagingItemReader<HostPoint> hostPointReader() {
        return new JpaPagingItemReaderBuilder<HostPoint>()
                .name("hostPointReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("SELECT h FROM HostPoint h ORDER BY h.id ASC")
                .build();
    }
}
