package com.chadev.xcape.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ReservationRepositoryTest {

    private final ReservationRepository reservationRepository;

    ReservationRepositoryTest(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }



    @BeforeEach
    void beforeEach() {
    }

    @Test
    void findReservationsByTheme() {
    }

    @Test
    void findReservationsByStartTimeBetweenAndTheme() {
    }
}