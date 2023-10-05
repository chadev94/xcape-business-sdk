package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findBannersByMerchantId(Long merchantId);
}
