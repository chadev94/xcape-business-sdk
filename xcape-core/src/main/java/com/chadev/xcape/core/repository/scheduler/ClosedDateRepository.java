package com.chadev.xcape.core.repository.scheduler;

import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.scheduler.ClosedDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClosedDateRepository extends JpaRepository<ClosedDate, Long> {

    List<ClosedDate> findClosedDatesByMerchant(Merchant merchant);

    ClosedDate findByMerchantAndClosedDate(Merchant merchant, LocalDate closedDate);
}