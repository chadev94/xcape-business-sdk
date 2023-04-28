package com.chadev.xcape.admin.service;

import com.chadev.xcape.core.domain.dto.scheduler.SchedulerDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.scheduler.Scheduler;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.scheduler.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;

    private final CoreMerchantRepository merchantRepository;

    public SchedulerDto turnOnScheduler(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        Scheduler scheduler = schedulerRepository.findByMerchant(merchant).orElseThrow(IllegalArgumentException::new);
        scheduler.setIsAwake(true);
        schedulerRepository.save(scheduler);
        return SchedulerDto.from(scheduler);
    }

    public SchedulerDto turnOffScheduler(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        Scheduler schedulerAwake = schedulerRepository.findByMerchant(merchant).orElseThrow(IllegalArgumentException::new);
        schedulerAwake.setIsAwake(false);
        schedulerRepository.save(schedulerAwake);
        return SchedulerDto.from(schedulerAwake);
    }

    public SchedulerDto updateTime(Long merchantId, LocalTime time) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        Scheduler scheduler = schedulerRepository.findByMerchant(merchant).orElseThrow(IllegalArgumentException::new);
        scheduler.setTime(time);
        schedulerRepository.save(scheduler);
        return SchedulerDto.from(scheduler);
    }

    public SchedulerDto getScheduler(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        Scheduler scheduler = schedulerRepository.findByMerchant(merchant).orElseThrow(IllegalArgumentException::new);
        return SchedulerDto.from(scheduler);
    }
}
