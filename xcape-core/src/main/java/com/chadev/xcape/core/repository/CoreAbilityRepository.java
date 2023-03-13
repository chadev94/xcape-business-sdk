package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Ability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreAbilityRepository extends JpaRepository<Ability, Long> {

    List<Ability> findAbilityListByThemeId(Long themeId);

    List<Ability> findAbilityListByMerchantId(Long merchantId);
}
