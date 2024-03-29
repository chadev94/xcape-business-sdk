package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findThemesByMerchantId(Long merchantId);

    @Query("select t from Theme t join fetch t.priceList where t.merchant.id = :merchantId")
    List<Theme> findThemesWithPriceListByMerchantId(Long merchantId);

    @Query("select t from Theme t join fetch t.timetableList where t.merchant = :merchant")
    List<Theme> findThemesWithTimeTableListByMerchantId(Merchant merchant);
}