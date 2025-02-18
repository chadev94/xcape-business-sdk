package com.chadev.xcape.core.domain.request;

import com.chadev.xcape.core.domain.dto.ReservationDetailDto;
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

    public NotificationTemplateEnum.ReservationCancelParam getReservationCancelParam(ReservationDetailDto reservationDetailDto, ObjectMapper objectMapper) {
        return new NotificationTemplateEnum.ReservationCancelParam(
                reservationDetailDto.getPhoneNumber(),
                reservationDetailDto.getDate().toString(),
                reservationDetailDto.getTime(),
                reservationDetailDto.getMerchantName(),
                reservationDetailDto.getMerchantNameForKakao(),
                reservationDetailDto.getThemeName(),
                reservationDetailDto.getReservedBy(),
                reservationDetailDto.getPhoneNumber(),
                reservationDetailDto.getParticipantCount().toString(),
                objectMapper
        );
    }
}
