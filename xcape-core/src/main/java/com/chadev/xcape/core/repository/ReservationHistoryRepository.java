package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {

    List<ReservationHistory> findReservationHistoriesByPhoneNumberOrderByRegisteredAt(String phoneNumber);

    ReservationHistory findById(String reservationHistoryId);

    @Query("select rh from ReservationHistory rh where rh.id = :reservationHistoryId")
    ReservationHistory findByReservationHistoryId(String reservationHistoryId);
}