package com.eatpizzaquickly.notificationservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class FailMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String message;

    private int partitionNumber;

    private Long offset;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private String errorType;

    private LocalDateTime failedAt;

    private int retryCount;

    private String status;


    @Builder
    public FailMessage(String message, int partitionNumber, Long offset, String errorMessage, String errorType, LocalDateTime failedAt, int retryCount, String status) {
        this.message = message;
        this.partitionNumber = partitionNumber;
        this.offset = offset;
        this.errorMessage = errorMessage;
        this.errorType = errorType;
        this.failedAt = failedAt;
        this.retryCount = retryCount;
        this.status = status;
    }
}
