package com.chadev.xcape.admin.repository;

import com.chadev.xcape.core.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select a from Account a left join fetch a.merchant where a.username = :username")
    Optional<Account> findByUsername(String username);
}
