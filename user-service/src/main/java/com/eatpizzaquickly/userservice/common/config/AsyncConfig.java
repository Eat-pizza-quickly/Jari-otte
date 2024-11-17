package com.eatpizzaquickly.userservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Bean(name = "emailExecutor")
    public Executor emailExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);      // 최소 스레드 수
        executor.setMaxPoolSize(5);       // 최대 스레드 수
        executor.setQueueCapacity(100);   // 대기열 크기
        executor.setThreadNamePrefix("EmailSender-");
        executor.initialize();
        return executor;
    }
}
