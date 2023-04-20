package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreThemeRepository extends JpaRepository<Theme, Long> {

    List<Theme> findThemesByMerchant(Merchant merchant);

    List<Theme> findThemesByMerchantId(Long merchantId);

//    @Query("select t from Theme t join fetch t.abilityList")
//    List<Theme> findThemesWithAbilityList();
}