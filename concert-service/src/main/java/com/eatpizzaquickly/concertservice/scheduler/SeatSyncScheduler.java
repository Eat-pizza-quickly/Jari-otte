package com.eatpizzaquickly.concertservice.scheduler;

import com.eatpizzaquickly.concertservice.entity.Concert;
import com.eatpizzaquickly.concertservice.repository.ConcertRepository;
import com.eatpizzaquickly.concertservice.service.ConcertService;
import com.eatpizzaquickly.concertservice.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SeatSyncScheduler {

    private final ConcertRepository concertRepository;
    private final SeatService seatService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    public void syncSeatsForEndedConcerts() {
        LocalDate today = LocalDate.now();
        List<Concert> endedConcerts = concertRepository.findByEndDateBefore(today);

        for (Concert concert : endedConcerts) {
            seatService.syncAvailableSeatsToDatabase(concert.getId());
        }
    }
}
