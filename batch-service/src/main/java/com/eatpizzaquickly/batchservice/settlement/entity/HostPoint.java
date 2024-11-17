package com.eatpizzaquickly.batchservice.settlement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class HostPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long payId;
    private Long hostId;
    private Long points;


    public HostPoint(Long payId, Long hostId, Long points) {
        this.payId = payId;
        this.hostId = hostId;
        this.points = points;
    }
}
