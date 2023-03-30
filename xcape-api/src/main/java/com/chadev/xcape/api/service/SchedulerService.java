package com.chadev.xcape.api.service;

import com.chadev.xcape.core.domain.dto.scheduler.SchedulerDto;
import com.chadev.xcape.core.domain.entity.scheduler.Scheduler;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.scheduler.ClosedDateRepository;
import com.chadev.xcape.core.repository.scheduler.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final ClosedDateRepository closedDateRepository;

    private final CoreMerchantRepository merchantRepository;

    public SchedulerDto turnOnScheduler(Long merchantId) {
        Scheduler schedulerAwake = schedulerRepository.findByMerchant(merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new));
        schedulerAwake.setIsAwake(true);
        schedulerRepository.save(schedulerAwake);
        return SchedulerDto.from(schedulerAwake);
    }

    public SchedulerDto turnOffScheduler(Long merchantId) {
        Scheduler schedulerAwake = schedulerRepository.findByMerchant(merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new));
        schedulerAwake.setIsAwake(false);
        schedulerRepository.save(schedulerAwake);
        return SchedulerDto.from(schedulerAwake);
    }

    public int merchantCount() {
        return merchantRepository.findAllMerchantsId().size();
    }
}
