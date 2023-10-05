package com.chadev.xcape.core.domain.request;

import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.service.notification.NotificationTemplateEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationCancelRequest {

    private String authenticationCode;

    private String requestId;

    private String recipientNo;

    public NotificationTemplateEnum.ReservationCancelParam getReservationCancelParam(ReservationDto reservationDto, ObjectMapper objectMapper) {
        return new NotificationTemplateEnum.ReservationCancelParam(
                reservationDto.getPhoneNumber(),
                reservationDto.getDate().toString(),
                reservationDto.getTime(),
                reservationDto.getMerchantName(),
                reservationDto.getThemeName(),
                reservationDto.getReservedBy(),
                reservationDto.getPhoneNumber(),
                reservationDto.getParticipantCount().toString(),
                reservationDto.getPrice().toString() + "원",
                objectMapper
        );
    }
}
