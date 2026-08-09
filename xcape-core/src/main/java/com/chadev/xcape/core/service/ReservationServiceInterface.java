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

    // notify(알림 발송)는 되돌릴 수 없는 외부 호출이므로 트랜잭션 커밋 이후 호출자가 별도로 실행해야 한다.
    @Transactional
    default ReservationHistoryDto registerProcess(String reservationId, ReservationRequest reservationRequest) {
        ReservationAuthenticationDto reservationAuthenticationDto = checkTimeOut(reservationRequest);
        checkAuthenticationCode(reservationAuthenticationDto, reservationRequest.getAuthenticationCode());
        return registerExecute(reservationId, reservationRequest);
    }

    @Transactional
    default ReservationHistoryDto cancelProcess(String reservationId, ReservationRequest reservationRequest) {
        ReservationAuthenticationDto reservationAuthenticationDto = checkTimeOut(reservationRequest);
        checkAuthenticationCode(reservationAuthenticationDto, reservationRequest.getAuthenticationCode());
        return cancelExecute(reservationId, reservationRequest);
    }
}
