package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {

    List<ReservationHistory> findReservationHistoriesByPhoneNumberOrderByDateTime(String phoneNumber);

    @Query("select rh, r.themeName, r.date, r.time, r.id " +
            "from ReservationHistory rh " +
            "join fetch rh.reservation r " +
            "where rh.phoneNumber = :phoneNumber")
    List<ReservationHistory> findReservationHistoriesByPhoneNumber(String phoneNumber);
}