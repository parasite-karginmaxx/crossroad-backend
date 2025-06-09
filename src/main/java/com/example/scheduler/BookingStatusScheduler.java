package com.example.scheduler;

import com.example.service.BookingStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingStatusScheduler {

    private final BookingStatusService statusService;

    @Scheduled(cron = "0 0 6 * * *") // каждый день в 6:00
    public void updateStatuses() {
        statusService.updateStatusAuto();
    }
}
