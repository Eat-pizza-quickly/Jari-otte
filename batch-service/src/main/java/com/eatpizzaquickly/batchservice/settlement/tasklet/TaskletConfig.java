package com.eatpizzaquickly.batchservice.settlement.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Configuration
@RequiredArgsConstructor
public class TaskletConfig {
    private final DataSource dataSource;

    public Tasklet deleteTempTableTasklet() {
        return ((contribution, chunkContext) -> {
            try (Connection connection = dataSource.getConnection()) {
                String deleteHostPointSql = "DELETE FROM host_point WHERE pay_id IN (" +
                        "SELECT payment_id FROM temp_payment WHERE settlement_status = 'SETTLED')";
                try (PreparedStatement hostPointStatement = connection.prepareStatement(deleteHostPointSql)) {
                    hostPointStatement.executeUpdate();
                }

                // 2. Temp_payment 테이블에서 status가 settled인 항목 삭제
                String deleteTempPaymentSql = "DELETE FROM Temp_payment WHERE settlement_status = 'SETTLED'";
                try (PreparedStatement tempPaymentStatement = connection.prepareStatement(deleteTempPaymentSql)) {
                    tempPaymentStatement.executeUpdate();
                }
            }
            return RepeatStatus.FINISHED;
        });
    }
}
