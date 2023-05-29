package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.dto.ReservationAuthenticationDto;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.request.ReservationRequest;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationServiceInterface {

    ReservationAuthenticationDto checkTimeOut(ReservationRequest reservationRequest);

    void checkAuthenticationCode(ReservationAuthenticationDto reservationAuthenticationDto, String authenticationCode);

    ReservationDto registerExecute(String reservationId, ReservationRequest reservationRequest);

    ReservationDto cancelExecute(String reservationHistoryId, ReservationRequest reservationRequest);

    void notify(ReservationDto reservation, ReservationRequest reservationRequest);

    @Transactional
    default ReservationDto registerProcess(String reservationId, ReservationRequest reservationRequest) {
        ReservationAuthenticationDto reservationAuthenticationDto = checkTimeOut(reservationRequest);
        checkAuthenticationCode(reservationAuthenticationDto, reservationRequest.getAuthenticationCode());
        ReservationDto savedReservation = registerExecute(reservationId, reservationRequest);
        notify(savedReservation, reservationRequest);
        return savedReservation;
    }

    @Transactional
    default void cancelProcess(String reservationId, ReservationRequest reservationRequest) {
        ReservationAuthenticationDto reservationAuthenticationDto = checkTimeOut(reservationRequest);
        checkAuthenticationCode(reservationAuthenticationDto, reservationRequest.getAuthenticationCode());
        ReservationDto deletedReservation = cancelExecute(reservationId, reservationRequest);
        notify(deletedReservation, reservationRequest);
    }
}
