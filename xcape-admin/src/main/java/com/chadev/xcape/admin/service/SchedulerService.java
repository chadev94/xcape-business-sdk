package com.chadev.xcape.admin.service;

import com.chadev.xcape.core.domain.dto.scheduler.ClosedDateDto;
import com.chadev.xcape.core.domain.dto.scheduler.SchedulerDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.scheduler.ClosedDate;
import com.chadev.xcape.core.domain.entity.scheduler.Scheduler;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.scheduler.ClosedDateRepository;
import com.chadev.xcape.core.repository.scheduler.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final ClosedDateRepository closedDateRepository;

    private final CoreMerchantRepository merchantRepository;

    public SchedulerDto turnOnScheduler(Long merchantId) {
        Scheduler scheduler = schedulerRepository.findByMerchant(merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new));
        scheduler.setIsAwake(true);
        schedulerRepository.save(scheduler);
        return SchedulerDto.from(scheduler);
    }

    public SchedulerDto turnOffScheduler(Long merchantId) {
        Scheduler schedulerAwake = schedulerRepository.findByMerchant(merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new));
        schedulerAwake.setIsAwake(false);
        schedulerRepository.save(schedulerAwake);
        return SchedulerDto.from(schedulerAwake);
    }

    public SchedulerDto updateTime(Long merchantId, LocalTime time) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        Scheduler scheduler = schedulerRepository.findByMerchant(merchant);
        scheduler.setTime(time);
        schedulerRepository.save(scheduler);
        return SchedulerDto.from(scheduler);
    }

    public ClosedDateDto registerClosedDate(Long merchantId, LocalDate date) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        ClosedDate savedClosedDate = closedDateRepository.save(new ClosedDate(merchant, date));
        return ClosedDateDto.from(savedClosedDate);
    }

    public ClosedDateDto deleteClosedDate(Long merchantId, LocalDate date) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        ClosedDate closedDate = closedDateRepository.findByMerchantAndClosedDate(merchant, date);
        ClosedDateDto closedDateDto = ClosedDateDto.from(closedDate);
        closedDateRepository.delete(closedDate);
        return closedDateDto;
    }

    public List<LocalDate> getClosedDates(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        return closedDateRepository.findClosedDatesByMerchant(merchant).stream().map((ClosedDate::getClosedDate)).toList();
    }

    public SchedulerDto getScheduler(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        Scheduler scheduler = schedulerRepository.findByMerchant(merchant);
        return SchedulerDto.from(scheduler);
    }
}
