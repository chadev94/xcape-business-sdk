package com.chadev.xcape.api.controller.request;

import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.service.notification.NotificationTemplateEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRegisterRequest {

    private String reservedBy;

    private String phoneNumber;

    private Integer participantCount;

    private String requestId;

    private String authenticationNumber;

    public NotificationTemplateEnum.ReservationSuccessParam getReservationSuccessParam(ReservationDto reservationDto, ObjectMapper objectMapper) {
        return new NotificationTemplateEnum.ReservationSuccessParam(
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
