package com.eatpizzaquickly.batchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BatchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchServiceApplication.class, args);
    }

}
