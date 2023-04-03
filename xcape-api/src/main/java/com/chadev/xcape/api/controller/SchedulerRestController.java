package com.chadev.xcape.api.controller;

import com.chadev.xcape.api.service.ReservationService;
import com.chadev.xcape.api.service.SchedulerService;
import com.chadev.xcape.core.domain.dto.scheduler.SchedulerDto;
import com.chadev.xcape.core.service.CoreMerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@EnableAsync
@RestController
public class SchedulerRestController {

    private final SchedulerService schedulerService;
    private final CoreMerchantService merchantService;
    private final ReservationService reservationService;

    // 지점별 빈 예약 생성
    @Async
    @Scheduled(cron = "0 0 0-6 * * *")  //  00시 ~ 06시 매시간 1분에 동작
    public void createBatchReservations() {
        int hour = LocalTime.now().getHour();
        LocalDate date = LocalDate.now().plusDays(30);
        log.info("date={}", date);

        merchantService.getMerchantIdList().forEach((merchantId) -> {
            SchedulerDto scheduler = schedulerService.getScheduler(merchantId);
            log.info("scheduler = [" +
                    "isAwake: {}, " +
                    "merchantId: {}," +
                    "time: {}]", scheduler.getIsAwake(), scheduler.getMerchantId(), scheduler.getTime());
            log.info(
                    "condition check = {}",
                    scheduler.getIsAwake() &&
                    scheduler.getTime().getHour() == hour &&
                    !schedulerService.getClosedDateList(merchantId).contains(date));
            if (
                    scheduler.getIsAwake() &&
                            scheduler.getTime().getHour() == hour &&
                            !schedulerService.getClosedDateList(merchantId).contains(date)
            ) {
                log.info("scheduler-on!!\nscheduler=[" +
                        "isAwake: {}, " +
                        "merchantId: {}," +
                        "time: {}]", scheduler.getIsAwake(), scheduler.getMerchantId(), scheduler.getTime());
                reservationService.createEmptyReservationByMerchantId(merchantId, date);
            }
        });
    }

    // 가예약 자동 취소
    @Async
    @Scheduled(cron = "0 * * * * *")
    public void autoCancelFakeReservation() {
        reservationService.getFakeReservationByLocalTime().forEach((reservation) -> reservationService.cancelReservationById(reservation.getId()));
    }
}
