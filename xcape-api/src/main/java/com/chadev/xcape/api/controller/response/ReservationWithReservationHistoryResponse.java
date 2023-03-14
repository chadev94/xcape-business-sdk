package com.chadev.xcape.api.controller.response;

import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWithReservationHistoryResponse {

    ReservationDto reservation;

    ReservationHistoryDto history;

    public static ReservationWithReservationHistoryResponse from(ReservationDto reservation, ReservationHistoryDto history) {
        return new ReservationWithReservationHistoryResponse(reservation, history);
    }
}
