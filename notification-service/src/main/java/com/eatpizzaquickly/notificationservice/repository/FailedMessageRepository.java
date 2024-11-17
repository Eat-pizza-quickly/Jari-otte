package com.eatpizzaquickly.notificationservice.repository;

import com.eatpizzaquickly.notificationservice.entity.FailMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedMessageRepository extends JpaRepository<FailMessage,Long> {
}
