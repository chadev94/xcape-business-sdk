package com.chadev.xcape.core.repository.scheduler;

import com.chadev.xcape.core.domain.entity.scheduler.ClosedDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClosedDateRepository extends JpaRepository<ClosedDate, Long> {
}