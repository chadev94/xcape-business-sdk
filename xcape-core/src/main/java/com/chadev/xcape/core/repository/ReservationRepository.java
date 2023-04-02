package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByThemeIdAndDate(Long themeId, LocalDate date);

    List<Reservation> findReservationsByThemeIdAndDateOrderById(Long themeId, LocalDate date);

//    @Query("select r from Reservation r where r.date = :date and r.unreservedTime between :preTime and :subTime")
//    List<Reservation> findAllByDate(Optional<LocalTime> prevTime, Optional<LocalTime> subTime, Optional<LocalDate> date);
}
