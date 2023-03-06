package com.chadev.xcape.admin.repository;

import com.chadev.xcape.core.domain.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {

    List<Price> getPriceListByThemeIdAndUseYn(Long themeId, Boolean useYn);
}
