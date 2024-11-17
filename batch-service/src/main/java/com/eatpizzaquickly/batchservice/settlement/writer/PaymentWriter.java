package com.eatpizzaquickly.batchservice.settlement.writer;


import com.eatpizzaquickly.batchservice.common.client.PaymentClient;
import com.eatpizzaquickly.batchservice.settlement.dto.request.PaymentRequestDto;
import com.eatpizzaquickly.batchservice.settlement.entity.TempPayment;
import com.eatpizzaquickly.batchservice.settlement.repository.TempPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.naming.ServiceUnavailableException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentWriter {

    private final PaymentClient paymentClient;
    private final TempPaymentRepository tempPaymentRepository;
    private final JdbcTemplate jdbcTemplate;

    public ItemWriter<TempPayment> tempPaymentWriter() {
        return tempPayments -> {
            String sql = "INSERT INTO temp_payment (payment_id, settlement_status, pay_status, amount, concert_id) " +
                    "VALUES (?, ?, ?, ?, ?) ";

            jdbcTemplate.batchUpdate(sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            TempPayment tempPayment = tempPayments.getItems().get(i);
                            ps.setLong(1, tempPayment.getPaymentId());
                            ps.setString(2, tempPayment.getSettlementStatus().name());
                            ps.setString(3, tempPayment.getPayStatus().name());
                            ps.setLong(4, tempPayment.getAmount());
                            ps.setLong(5, tempPayment.getConcertId());
                        }

                        @Override
                        public int getBatchSize() {
                            return tempPayments.size();
                        }
                    });
        };
    }

    public ItemWriter<PaymentRequestDto> paymentWriter() {
        return payments -> {
            ResponseEntity<String> response = paymentClient.updatePayments((List<PaymentRequestDto>) payments.getItems());

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Payment Update Successfully");
            } else {
                log.error("Payment Update fail : {}", response.getStatusCode());
                throw new ServiceUnavailableException("Payment Update Fail");
            }
        };
    }
}
