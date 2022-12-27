package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreThemeRepository extends JpaRepository<Theme, Long> {

    List<Theme> findAll();

    List<Theme> findThemesByMerchant(Merchant merchant);
}