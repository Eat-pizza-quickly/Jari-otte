package com.eatpizzaquickly.batchservice.settlement.repository;

import com.eatpizzaquickly.batchservice.settlement.entity.HostPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostPointRepository extends JpaRepository<HostPoint, Long> {

    List<HostPoint> findByHostIdIn(List<Long> hostIds);
    Page<HostPoint> findAll(Pageable pageable);

    List<HostPoint> findByPayIdIn(List<Long> payIds);
}
