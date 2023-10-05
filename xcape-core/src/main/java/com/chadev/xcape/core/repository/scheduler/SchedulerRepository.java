package com.chadev.xcape.core.repository.scheduler;

import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.scheduler.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

    Optional<Scheduler> findByMerchant(Merchant merchant);
}