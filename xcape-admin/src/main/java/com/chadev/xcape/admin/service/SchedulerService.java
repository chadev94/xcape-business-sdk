package com.chadev.xcape.admin.service;

import com.chadev.xcape.core.domain.dto.scheduler.SchedulerDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.scheduler.Scheduler;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.scheduler.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final ReservationService reservationService;
    private final SchedulerRepository schedulerRepository;
    private final CoreMerchantRepository coreMerchantRepository;

    public SchedulerDto turnOnScheduler(Long merchantId) {
        Merchant merchant = coreMerchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        Scheduler scheduler = schedulerRepository.findByMerchant(merchant).orElseThrow(IllegalArgumentException::new);
        scheduler.setIsAwake(true);
        schedulerRepository.save(scheduler);
        return SchedulerDto.from(scheduler);
    }

    public SchedulerDto turnOffScheduler(Long merchantId) {
        Merchant merchant = coreMerchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        Scheduler schedulerAwake = schedulerRepository.findByMerchant(merchant).orElseThrow(IllegalArgumentException::new);
        schedulerAwake.setIsAwake(false);
        schedulerRepository.save(schedulerAwake);
        return SchedulerDto.from(schedulerAwake);
    }

    public SchedulerDto updateTime(Long merchantId, Integer time) {
        Merchant merchant = coreMerchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        Scheduler scheduler = schedulerRepository.findByMerchant(merchant).orElseThrow(IllegalArgumentException::new);
        scheduler.setTime(LocalTime.of(time, 0));
        Scheduler savedScheduler = schedulerRepository.save(scheduler);
        return SchedulerDto.from(savedScheduler);
    }

    public SchedulerDto getScheduler(Merchant merchant) {
        Scheduler scheduler = schedulerRepository.findByMerchant(merchant).orElseThrow(IllegalArgumentException::new);
        return SchedulerDto.from(scheduler);
    }

    public void createBatchReservations() {
        int hour = LocalTime.now().getHour();
        LocalDate date = LocalDate.now().plusDays(21);

        log.info("createBatchReservations >>>>> server hour: {}", hour);
        coreMerchantRepository.findAll().forEach((merchant) -> {
            SchedulerDto scheduler = getScheduler(merchant);
            log.info("{} scheduler hour: {}", merchant.getName(), scheduler.getTime().getHour());
            if (scheduler.getIsAwake() && scheduler.getTime().getHour() == hour) {
                reservationService.createEmptyReservationByMerchant(merchant, date);
            }
        });
    }
}
