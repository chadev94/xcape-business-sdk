package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreMerchantRepository extends JpaRepository<Merchant, Long> {

    @Query("select m from Merchant m left join fetch m.themeList t order by m.id, t.id")
    List<Merchant> findAllWithThemes();

    List<Merchant> findAll();

//    List<Merchant> findMerchantsByAccount(Account account);

    Merchant findByName(String name);

}