package com.eatpizzaquickly.reservationservice;

import com.eatpizzaquickly.reservationservice.payment.entity.PayMethod;
import com.eatpizzaquickly.reservationservice.payment.entity.PayStatus;
import com.eatpizzaquickly.reservationservice.payment.entity.SettlementStatus;
import com.eatpizzaquickly.reservationservice.reservation.entity.ReservationStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
public class PaymentDummyBatchInsert {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test

    public void batchInsertReservationsAndPayments() {
        final int BATCH_SIZE = 1000;
        final int TOTAL_RECORDS_PER_USER = 200000;
        final long[] USER_IDS = {2L, 3L, 4L};

        for (long userId : USER_IDS) {
            // Step 1: 배치로 Reservation 삽입
            String reservationSql = "INSERT INTO reservation (price, status, created_at, user_id, seat_id, concert_id) VALUES (?, ?, ?, ?, ?, ?)";

            jdbcTemplate.batchUpdate(reservationSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, 100); // price (모든 예약의 가격 동일하게 설정)
                    ps.setString(2, ReservationStatus.CONFIRMED.name()); // status
                    ps.setTimestamp(3, java.sql.Timestamp.valueOf(i < TOTAL_RECORDS_PER_USER * 0.1 ? LocalDateTime.now() : LocalDateTime.now().minusDays(7))); // created_at (10% 오늘, 90% 일주일 전)
                    ps.setLong(4, userId); // user_id
                    ps.setLong(5, 1L); // seat_id (예시 값)
                    ps.setLong(6, userId); // concert_id (userId와 동일하게 설정)
                }

                @Override
                public int getBatchSize() {
                    return TOTAL_RECORDS_PER_USER;
                }
            });

            log.info("User ID {} 및 Concert ID {}에 대한 {}개의 Reservation이 삽입되었습니다.", userId, userId, TOTAL_RECORDS_PER_USER);

            // Step 2: Reservation ID 예측 (AUTO_INCREMENT를 사용하는 경우)
            // 만약 기존에 데이터가 없다고 가정하면, 첫 Reservation의 ID는 1이고, 이후 10000개는 순차적으로 증가합니다.
            // 실제로는 데이터베이스 설정과 현재 데이터 상태에 따라 다를 수 있습니다.
            // 따라서, 이 예제에서는 Reservation ID가 1부터 200000까지 있다고 가정합니다.

            // Step 3: 배치로 Payment 삽입
            String paymentSql = "INSERT INTO payments (pay_uid, amount, pay_info, pay_method, pay_status, payment_key, reservation_id, settlement_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.batchUpdate(paymentSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, "payUid-" + (i + 1)); // pay_uid
                    ps.setLong(2, 1000L); // amount (모든 결제의 금액 동일하게 설정)
                    ps.setString(3, "Test Payment " + (i + 1)); // pay_info
                    ps.setString(4, PayMethod.TOSS.name()); // pay_method
                    ps.setString(5, PayStatus.PAID.name()); // pay_status
                    ps.setString(6, "paymentKey-" + (i + 1)); // payment_key
                    ps.setLong(7, (userId - 2) * TOTAL_RECORDS_PER_USER + i + 1L); // reservation_id (1부터 600000까지)
                    ps.setString(8, SettlementStatus.UNSETTLED.name()); // settlement_status
                }

                @Override
                public int getBatchSize() {
                    return TOTAL_RECORDS_PER_USER;
                }
            });

            log.info("User ID {} 및 Concert ID {}에 대한 {}개의 Payment이 삽입되었습니다.", userId, userId, TOTAL_RECORDS_PER_USER);
        }
    }
}
