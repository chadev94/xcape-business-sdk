package com.chadev.xcape.admin.repository;

import com.chadev.xcape.core.domain.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    List<Theme> findThemesByMerchantId(Long merchantId);

    @Query("select distinct t from Theme t join fetch t.priceList join fetch t.abilityList where t.id in (:themeId)")
    Theme findThemeWithPriceAndAbilityByThemeId(Long themeId);
}
