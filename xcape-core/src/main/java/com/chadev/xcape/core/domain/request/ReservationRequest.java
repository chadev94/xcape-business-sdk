package com.chadev.xcape.core.domain.request;

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
public class ReservationRequest {

    private String reservedBy;

    private String phoneNumber;

    private Integer participantCount;

    private String requestId;

    private String authenticationCode;

    private String recipientNo;

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
