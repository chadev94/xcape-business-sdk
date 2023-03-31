package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.repository.mapping.MerchantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreMerchantRepository extends JpaRepository<Merchant, Long> {

    @Query("select m from Merchant m left join fetch m.themeList t order by m.id, t.id")
    List<Merchant> findAllMerchantsWithThemes();

    @Query("select m.id from Merchant m")
    List<Long> findAllMerchantsId();

    List<MerchantInfo> findAllBy();
}