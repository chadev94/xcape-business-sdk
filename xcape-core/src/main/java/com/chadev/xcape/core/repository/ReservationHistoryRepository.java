package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {

    List<ReservationHistory> findReservationHistoriesByPhoneNumberOrderByRegisteredAtDesc(String phoneNumber);

    ReservationHistory findById(String reservationHistoryId);

    List<ReservationHistory> findReservationHistoriesByReservationSeqOrderByRegisteredAt(long reservationSeq);
}