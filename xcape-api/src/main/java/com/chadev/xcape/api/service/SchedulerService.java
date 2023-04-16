package com.chadev.xcape.api.service;

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
import java.util.List;

@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final ClosedDateRepository closedDateRepository;

    private final CoreMerchantRepository merchantRepository;

    public SchedulerDto getScheduler(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        Scheduler scheduler = schedulerRepository.findByMerchant(merchant).orElseThrow(IllegalArgumentException::new);
        return SchedulerDto.from(scheduler);
    }

    public List<LocalDate> getClosedDateList(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        List<ClosedDate> closedDates = closedDateRepository.findClosedDatesByMerchant(merchant);
        return closedDates.stream().map((entity) -> ClosedDateDto.from(entity).getClosedDate()).toList();
    }
}
