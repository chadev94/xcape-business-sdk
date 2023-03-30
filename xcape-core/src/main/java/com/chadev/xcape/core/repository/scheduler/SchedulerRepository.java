package com.chadev.xcape.core.repository.scheduler;

import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.scheduler.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

    Scheduler findByMerchant(Merchant merchant);
}