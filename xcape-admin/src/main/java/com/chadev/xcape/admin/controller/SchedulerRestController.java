package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.service.ReservationService;
import com.chadev.xcape.admin.service.SchedulerService;
import com.chadev.xcape.core.domain.dto.scheduler.SchedulerDto;
import com.chadev.xcape.core.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@EnableScheduling
@EnableAsync
@Slf4j
@RequiredArgsConstructor
@RestController
public class SchedulerRestController {

    private final SchedulerService schedulerService;
    private final ReservationService reservationService;

    @Async
    @Scheduled(cron = "0 0 0-6 * * *", zone = "Asia/Seoul")  //  00시 ~ 06시 매시간 1분에 동작
    public void createBatchReservations() {
        schedulerService.createBatchReservations();
    }

    // 가예약 자동 취소
    @Async
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
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
