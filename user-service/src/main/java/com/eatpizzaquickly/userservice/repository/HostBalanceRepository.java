package com.eatpizzaquickly.userservice.repository;


import com.eatpizzaquickly.userservice.entity.HostBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostBalanceRepository extends JpaRepository<HostBalance,Long> {

    Optional<HostBalance> findByHostId(Long hostId);
}
