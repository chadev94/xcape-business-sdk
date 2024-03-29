package com.chadev.xcape.core.domain.dto.history;

import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.domain.type.HistoryType;
import com.chadev.xcape.core.domain.type.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationHistoryDto {

    private Long reservationSeq;

    private Long seq;

    private String id;

    private String reservationId;

    private HistoryType type;

    private String reservedBy;

    private String phoneNumber;

    private Integer participantCount;

    private Integer price;

    private String merchantName;

    private String themeName;

    private String date;

    private String time;

    private RoomType roomType;

    private String registeredAt;

    public ReservationHistoryDto(ReservationHistory entity) {
        this.seq = entity.getSeq();
        this.reservationSeq = entity.getReservation().getSeq();
        this.id = entity.getId();
        this.type = entity.getType();
        this.reservedBy = entity.getReservedBy();
        this.phoneNumber = entity.getPhoneNumber();
        this.participantCount = entity.getParticipantCount();
        this.price = entity.getPrice();
        this.merchantName = entity.getMerchantName();
        this.themeName = entity.getThemeName();
        this.date = entity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = entity.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.roomType = entity.getRoomType();
        this.registeredAt = entity.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
