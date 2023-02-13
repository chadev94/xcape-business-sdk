package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {

    ReservationHistory findFirstByReservationOrderByIdDesc(Reservation reservation);
}