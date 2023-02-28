package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.repository.mapping.ReservationInfo;
import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findReservationsByTheme(Theme theme);

    List<ReservationInfo> findByThemeAndDate(Theme theme, LocalDate date);

    List<Reservation> findReservationsByIsReservedAndMerchant(Boolean isReserved, Merchant merchant);

    List<Reservation> findReservationsByThemeAndDateOrderById(Theme theme, LocalDate date);
}
