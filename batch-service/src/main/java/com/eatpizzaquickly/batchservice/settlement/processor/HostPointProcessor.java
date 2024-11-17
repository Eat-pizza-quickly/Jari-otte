package com.eatpizzaquickly.batchservice.settlement.processor;


import com.eatpizzaquickly.batchservice.settlement.dto.request.HostPointRequestDto;
import com.eatpizzaquickly.batchservice.settlement.entity.HostPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class HostPointProcessor {
    public ItemProcessor<HostPoint, HostPointRequestDto> pointTransmissionProcessor() {
        return HostPointRequestDto::from;
    }
}
