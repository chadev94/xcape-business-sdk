package com.chadev.xcape.core.domain.dto.history;

import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.domain.type.HistoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationHistoryDto {

    private Long id;

    private String reservationId;

    private HistoryType type;

    private LocalDateTime dateTime;

    private String reservedBy;

    private String phoneNumber;

    private Integer participantCount;

    private String roomType;

    private Integer price;

    public ReservationHistoryDto(ReservationHistory entity){
        this.id = entity.getId();
        this.reservationId = entity.getReservation().getId();
        this.type = entity.getType();
        this.dateTime = entity.getDateTime();
        this.reservedBy = entity.getReservedBy();
        this.phoneNumber = entity.getPhoneNumber();
        this.participantCount = entity.getParticipantCount();
        this.roomType = entity.getRoomType();
        this.price = entity.getPrice();
    }
}
