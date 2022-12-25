package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}