package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByThemeIdAndDate(Long themeId, LocalDate date);

    List<Reservation> findReservationsByThemeIdAndDateOrderById(Long themeId, LocalDate date);
}
