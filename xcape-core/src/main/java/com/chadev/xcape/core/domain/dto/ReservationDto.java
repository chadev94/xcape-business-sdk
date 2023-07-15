package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.domain.type.RoomType;
import com.chadev.xcape.core.service.notification.NotificationTemplateEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReservationDto {

    // 기본값
    private String id;

    private Long seq;

    private String merchantName;

    private Long themeId;

    private String themeName;

    private String date;

    private String time;

    private Boolean isReserved;

    // 예약 등록 시 설정되는 값
    private String reservedBy;

    private String phoneNumber;

    private Integer participantCount;

    private Integer price;

    private String reservationHistoryId;

    private LocalTime unReservedTime;

    private RoomType roomType;

    public ReservationDto(Reservation entity) {
        this.id = entity.getId();
        this.seq = entity.getSeq();
        this.merchantName = entity.getMerchantName();
        this.themeId = entity.getThemeId();
        this.themeName = entity.getThemeName();
        this.date = entity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = entity.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.isReserved = entity.getIsReserved();
        this.reservedBy = entity.getReservedBy();
        this.phoneNumber = entity.getPhoneNumber();
        this.participantCount = entity.getParticipantCount();
        this.price = entity.getPrice();
        this.unReservedTime = entity.getUnreservedTime();
        this.roomType = entity.getRoomType();
    }

    public String getReservedBy() {
        if (reservedBy != null) {
            return reservedBy;
        }

        return RoomType.OPEN_ROOM.equals(roomType) ? RoomType.OPEN_ROOM.name() : "X";
    }

    // for fake reservation
    public static ReservationDto fake(Reservation entity) {
        return ReservationDto.builder()
                .id(entity.getId())
                .seq(entity.getSeq())
                .merchantName(entity.getMerchantName())
                .themeId(entity.getThemeId())
                .themeName(entity.getThemeName())
                .date(entity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .time(entity.getTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .isReserved(true)
                .reservedBy("엑스케이프")
                .phoneNumber("0")
                .participantCount(0)
                .price(0)
                .reservationHistoryId(UUID.randomUUID().toString())
                .build();
    }

    public NotificationTemplateEnum.ReservationSuccessParam getReservationSuccessParam(ObjectMapper objectMapper) {
        return new NotificationTemplateEnum.ReservationSuccessParam(
                this.getPhoneNumber(),
                this.getDate().toString(),
                this.getTime(),
                this.getMerchantName(),
                this.getThemeName(),
                this.getReservedBy(),
                this.getPhoneNumber(),
                this.getParticipantCount().toString(),
                this.getPrice().toString() + "원",
                objectMapper
        );
    }

    public NotificationTemplateEnum.ReservationCancelParam getReservationCancelParam(ObjectMapper objectMapper) {
        return new NotificationTemplateEnum.ReservationCancelParam(
                this.getPhoneNumber(),
                this.getDate().toString(),
                this.getTime(),
                this.getMerchantName(),
                this.getThemeName(),
                this.getReservedBy(),
                this.getPhoneNumber(),
                this.getParticipantCount().toString(),
                this.getPrice().toString() + "원",
                objectMapper
        );
    }
}
