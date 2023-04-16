package com.chadev.xcape.api.repository;

import com.chadev.xcape.core.domain.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
}
