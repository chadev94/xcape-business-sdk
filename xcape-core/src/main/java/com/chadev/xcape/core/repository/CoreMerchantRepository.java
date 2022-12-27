package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Account;
import com.chadev.xcape.core.domain.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreMerchantRepository extends JpaRepository<Merchant, Long> {

    List<Merchant> findAll();

    List<Merchant> findMerchantsByAccount(Account account);

    Merchant findByName(String name);

}