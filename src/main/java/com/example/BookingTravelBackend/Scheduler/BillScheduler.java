package com.example.BookingTravelBackend.Scheduler;

import com.example.BookingTravelBackend.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BillScheduler {
    private final BillService billService;
    @Scheduled(fixedRate = 5  * 60 * 1000) // Chạy mỗi 5 phút
    public void checkAndCancelUnpaidOrders() {
        billService.cancelUnpaidOrders();
    }
}
