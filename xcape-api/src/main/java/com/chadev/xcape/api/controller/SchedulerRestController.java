package com.chadev.xcape.api.controller;

import com.chadev.xcape.api.service.SchedulerService;
import com.chadev.xcape.core.domain.dto.scheduler.SchedulerDto;
import com.chadev.xcape.core.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SchedulerRestController {

    private final SchedulerService schedulerService;

    @PutMapping("/turn-on")
    public Response<SchedulerDto> turnOnScheduler(Long merchantId) throws IllegalAccessException {
        return Response.success(schedulerService.turnOnScheduler(merchantId));
    }

    @PutMapping("/turn-off")
    public Response<SchedulerDto> turnOffScheduler(Long merchantId) throws IllegalAccessException {
        return Response.success(schedulerService.turnOffScheduler(merchantId));
    }

    @GetMapping("/test")
    public String test() {
        return "";
    }
}
