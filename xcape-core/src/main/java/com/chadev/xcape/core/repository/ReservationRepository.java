package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(String reservationId);

    List<Reservation> findByIdIn(List<String> reservationIdList);

    List<Reservation> findByThemeIdAndDateOrderBySeq(Long themeId, LocalDate date);

    List<Reservation> findReservationsByDateAndUnreservedTime(LocalDate date, LocalTime unreservedTime);

    List<Reservation> findByMerchantIdAndDateBetweenAndTimeBetween(Long merchantId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);

    List<Reservation> findByThemeIdAndDateBetweenAndTimeBetween(Long themeId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);

    List<Reservation> findByMerchantIdAndDateOrderBySeq(Long merchantId, LocalDate date);

    List<Reservation> findByIsReservedAndDateAndTimeBetween(boolean isReserved, LocalDate date, LocalTime startTime, LocalTime endTime);
    @Procedure("reservation_batch")
    void reservationBatch(LocalDate date);
}