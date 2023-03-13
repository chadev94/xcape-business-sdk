package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Price;
import com.chadev.xcape.core.domain.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorePriceRepository extends JpaRepository<Price, Long> {

    Price findByThemeAndPersonAndType(Theme theme, Integer person, String type);

    List<Price> findPricesByThemeIdAndUseYn(Long themeId, Boolean useYn);

    List<Price> findPriceListByMerchantIdAndUseYn(Long merchantId, boolean y);
}