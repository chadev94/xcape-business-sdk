package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.dto.ReservationAuthenticationDto;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.request.ReservationRequest;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationServiceInterface {

    ReservationAuthenticationDto checkTimeOut(ReservationRequest reservationRequest);

    void checkAuthenticationCode(ReservationAuthenticationDto reservationAuthenticationDto, String authenticationCode);

    ReservationHistoryDto registerExecute(String reservationId, ReservationRequest reservationRequest);

    ReservationHistoryDto cancelExecute(String reservationHistoryId, ReservationRequest reservationRequest);

    void notify(ReservationHistoryDto reservation, ReservationRequest reservationRequest);

    @Transactional
    default ReservationHistoryDto registerProcess(String reservationId, ReservationRequest reservationRequest) {
        ReservationAuthenticationDto reservationAuthenticationDto = checkTimeOut(reservationRequest);
        checkAuthenticationCode(reservationAuthenticationDto, reservationRequest.getAuthenticationCode());
        ReservationHistoryDto savedReservationHistoryDto = registerExecute(reservationId, reservationRequest);
        notify(savedReservationHistoryDto, reservationRequest);
        return savedReservationHistoryDto;
    }

    @Transactional
    default void cancelProcess(String reservationId, ReservationRequest reservationRequest) {
        ReservationAuthenticationDto reservationAuthenticationDto = checkTimeOut(reservationRequest);
        checkAuthenticationCode(reservationAuthenticationDto, reservationRequest.getAuthenticationCode());
        ReservationHistoryDto deletedReservationHistoryDto = cancelExecute(reservationId, reservationRequest);
        notify(deletedReservationHistoryDto, reservationRequest);
    }
}
