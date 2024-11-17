package com.eatpizzaquickly.userservice;

import com.eatpizzaquickly.userservice.common.config.PasswordEncoder;
import com.eatpizzaquickly.userservice.entity.UserRole;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserDummyTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int BATCH_SIZE = 1000;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Disabled("테스트용으로 더미 데이터를 한번만 생성하는 테스트입니다. 필요할 때만 활성화하세요.")
    public void batchInsertDummyUsers() {
        String sql = "INSERT INTO user (email, password, nickname, is_delete, user_role) VALUES (?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (int i = 1; i <= 50000; i++) {
            Object[] user = new Object[]{
                    "user" + i + "@example.com",
                    passwordEncoder.encode("password" + i),
                    "nickname" + i,
                    false,
                    UserRole.USER.name()
            };
            batchArgs.add(user);

            if (i % BATCH_SIZE == 0 || i == 50000) {
                jdbcTemplate.batchUpdate(sql, batchArgs);
                batchArgs.clear(); // 삽입 후 리스트 초기화
            }
        }
        System.out.println("Batch insert of dummy users completed.");
    }
}