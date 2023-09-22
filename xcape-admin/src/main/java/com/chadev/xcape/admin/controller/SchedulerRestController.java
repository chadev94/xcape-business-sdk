package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.controller.request.SchedulerUpdateRequest;
import com.chadev.xcape.admin.service.MockReservationService;
import com.chadev.xcape.admin.service.ReservationService;
import com.chadev.xcape.admin.service.SchedulerService;
import com.chadev.xcape.core.domain.dto.scheduler.SchedulerDto;
import com.chadev.xcape.core.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@EnableScheduling
@EnableAsync
@Slf4j
@RequiredArgsConstructor
@RestController
public class SchedulerRestController {

    private final SchedulerService schedulerService;
    private final MockReservationService mockReservationService;
    private final ReservationService reservationService;
//    @Async
//    @Scheduled(cron = "0 0 0-6 * * *")
//    public void createBatchReservations() {
//        log.info("createBatchReservations >>> {} >>>> batch start: {}", Thread.currentThread(), LocalDateTime.now());
//        schedulerService.createBatchReservations();
//        log.info("createBatchReservations >>> {} >>>> batch end: {}", Thread.currentThread(), LocalDateTime.now());
//    }

    // 가예약 자동 취소
    @Async
    @Scheduled(cron = "0 * 9-23 * * *")
    public void autoCancelFakeReservation() {
        log.info("autoCancelFakeReservation >>>> server time: {}", LocalTime.now());
        mockReservationService.cancelUnreservedMockReservations();
    }

    // 리마인더
    @Scheduled(cron = "*/10 * * * * *")
    public void reminder() {
        reservationService.reservationReminder();
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
    @PutMapping(value = "/schedulers")
    public Response<SchedulerDto> updateSchedulerTime(@RequestBody SchedulerUpdateRequest request) {
        SchedulerDto updatedScheduler = schedulerService.updateTime(request.getMerchantId(), request.getTime());
        return Response.success(updatedScheduler);
    }
}
