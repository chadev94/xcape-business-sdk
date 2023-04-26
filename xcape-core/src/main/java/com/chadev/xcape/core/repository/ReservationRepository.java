package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(String reservationId);

    List<Reservation> findByThemeIdAndDate(Long themeId, LocalDate date);

    List<Reservation> findReservationsByThemeIdAndDateOrderById(Long themeId, LocalDate date);

    List<Reservation> findReservationsByUnreservedTimeBetweenAndDate(LocalTime unreservedTime, LocalTime unreservedTime2, LocalDate date);

    Optional<Reservation> findBySeq(Long reservationSeq);
}
