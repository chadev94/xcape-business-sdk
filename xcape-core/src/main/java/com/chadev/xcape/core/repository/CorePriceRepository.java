package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Price;
import com.chadev.xcape.core.domain.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorePriceRepository extends JpaRepository<Price, Long> {

    Price findFirstByThemeAndPerson(Theme theme, Integer person);

    List<Price> findPriceListByThemeId(Long themeId);
}