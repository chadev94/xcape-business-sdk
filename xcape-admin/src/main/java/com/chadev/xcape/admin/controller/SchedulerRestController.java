package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.service.ReservationService;
import com.chadev.xcape.admin.service.SchedulerService;
import com.chadev.xcape.core.domain.dto.scheduler.SchedulerDto;
import com.chadev.xcape.core.response.Response;
import com.chadev.xcape.core.service.CoreMerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SchedulerRestController {

    private final SchedulerService schedulerService;
    private final CoreMerchantService merchantService;
    private final ReservationService reservationService;


    @Async
    @Scheduled(cron = "0 0 0-6 * * *")  //  00시 ~ 06시 매시간 1분에 동작
    public void createBatchReservations() {
        int hour = LocalTime.now().getHour();
        LocalDate date = LocalDate.now().plusDays(30);

        merchantService.getMerchantIdList().forEach((merchantId) -> {
            SchedulerDto scheduler = schedulerService.getScheduler(merchantId);
            if (scheduler.getIsAwake() && scheduler.getTime().getHour() == hour) {
                reservationService.createEmptyReservationByMerchantId(merchantId, date);
            }
        });
    }

    // 가예약 자동 취소
    @Async
    @Scheduled(cron = "0 * * * * *")
    public void autoCancelFakeReservation() {
        reservationService.getFakeReservationByLocalTime().forEach((reservation) -> reservationService.cancelFakeReservationById(reservation.getId()));
    }

    @PutMapping("/schedulers/on")
    public Response<SchedulerDto> turnOnScheduler(Long merchantId) {
        return Response.success(schedulerService.turnOnScheduler(merchantId));
    }

    @PutMapping("/schedulers/off")
    public Response<SchedulerDto> turnOffScheduler(Long merchantId) {
        return Response.success(schedulerService.turnOffScheduler(merchantId));
    }

    // 스케줄러 시간 변경
    @PutMapping(value = "/schedulers", params = {"time", "merchantId"})
    public Response<SchedulerDto> updateSchedulerTime(@DateTimeFormat(pattern = "HH") LocalTime time, Long merchantId) {
        return Response.success(schedulerService.updateTime(merchantId, time));
    }
}
