package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.service.SchedulerService;
import com.chadev.xcape.core.domain.dto.scheduler.ClosedDateDto;
import com.chadev.xcape.core.domain.dto.scheduler.SchedulerDto;
import com.chadev.xcape.core.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SchedulerRestController {

    private final SchedulerService schedulerService;

    @PutMapping("/schedulers/on")
    public Response<SchedulerDto> turnOnScheduler(Long merchantId) {
        return Response.success(schedulerService.turnOnScheduler(merchantId));
    }

    @PutMapping("/schedulers/off")
    public Response<SchedulerDto> turnOffScheduler(Long merchantId) {
        return Response.success(schedulerService.turnOffScheduler(merchantId));
    }

    @PutMapping(value = "/schedulers", params = {"time", "merchantId"})
    public Response<SchedulerDto> updateSchedulerTime(@DateTimeFormat(pattern = "HH") LocalTime time, Long merchantId) {
        return Response.success(schedulerService.updateTime(merchantId, time));
    }

    @PostMapping(value = "/schedulers/closed-dates", params = {"merchantId", "date"})
    public Response<ClosedDateDto> registerClosedDate(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, Long merchantId) {
        return Response.success(schedulerService.registerClosedDate(merchantId, date));
    }

    @DeleteMapping(value = "/schedulers/closed-dates", params = {"merchantId", "date"})
    public Response<ClosedDateDto> deleteClosedDate(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, Long merchantId) {
        return Response.success(schedulerService.deleteClosedDate(merchantId, date));
    }
}
