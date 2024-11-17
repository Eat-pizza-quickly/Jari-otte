package com.eatpizzaquickly.reservationservice.payment.repository;

import com.eatpizzaquickly.reservationservice.payment.entity.HostPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostPointRepository extends JpaRepository<HostPoint,Long> {
    Page<HostPoint> findAll(Pageable pageable);

    List<HostPoint> findByHostIdIn(List<Long> hostIds);
}
