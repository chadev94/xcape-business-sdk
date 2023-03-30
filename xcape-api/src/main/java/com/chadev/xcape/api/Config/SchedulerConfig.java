package com.chadev.xcape.api.Config;

import com.chadev.xcape.api.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SchedulerConfig {

    private final SchedulerService schedulerService;

    private final Runnable runnable = () -> {
    };

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduling() {
        this.runnable.run();
    }
}
