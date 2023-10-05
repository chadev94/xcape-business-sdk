package com.chadev.xcape.core.domain.request;

import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.type.RoomType;
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

    private RoomType roomType;

    public NotificationTemplateEnum.ReservationSuccessParam getReservationSuccessParam(ReservationHistoryDto reservationHistoryDto, ObjectMapper objectMapper) {
        return new NotificationTemplateEnum.ReservationSuccessParam(
                reservationHistoryDto.getPhoneNumber(),
                reservationHistoryDto.getDate(),
                reservationHistoryDto.getTime(),
                reservationHistoryDto.getMerchantName(),
                reservationHistoryDto.getThemeName(),
                reservationHistoryDto.getReservedBy(),
                reservationHistoryDto.getPhoneNumber(),
                reservationHistoryDto.getParticipantCount().toString(),
                reservationHistoryDto.getPrice().toString() + "원",
                objectMapper
        );
    }

    public NotificationTemplateEnum.ReservationCancelParam getReservationCancelParam(ReservationHistoryDto reservationHistoryDto, ObjectMapper objectMapper) {
        return new NotificationTemplateEnum.ReservationCancelParam(
                reservationHistoryDto.getPhoneNumber(),
                reservationHistoryDto.getDate(),
                reservationHistoryDto.getTime(),
                reservationHistoryDto.getMerchantName(),
                reservationHistoryDto.getThemeName(),
                reservationHistoryDto.getReservedBy(),
                reservationHistoryDto.getPhoneNumber(),
                reservationHistoryDto.getParticipantCount().toString(),
                reservationHistoryDto.getPrice().toString() + "원",
                objectMapper
        );
    }
}
