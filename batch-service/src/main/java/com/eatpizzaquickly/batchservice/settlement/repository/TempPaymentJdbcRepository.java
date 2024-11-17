package com.eatpizzaquickly.batchservice.settlement.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TempPaymentJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional
    public int updateSettlementStatus(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        log.info("정산 상태 변경 {} records", ids.size());

        try {
            String sql = "UPDATE temp_payment " +
                    "SET settlement_status = 'SETTLED' " +
                    "WHERE payment_id IN (:ids)";

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("ids", ids);

            int updatedCount = jdbcTemplate.update(sql, parameters);
            log.info("업데이트 성공 {} records", updatedCount);

            return updatedCount;

        } catch (Exception e) {
            log.error("업데이트 실패 status: {}", e.getMessage(), e);
            throw new RuntimeException("정산 상태 변경 실패", e);
        }
    }
}
