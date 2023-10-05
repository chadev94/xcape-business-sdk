package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.ReservationAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationAuthenticationRepository extends JpaRepository<ReservationAuthentication, String> {
}