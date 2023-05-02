package com.chadev.xcape.core.response;

import com.chadev.xcape.core.domain.entity.history.ReservationHistory;
import com.chadev.xcape.core.domain.type.HistoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationHistoryTableDto {
    private String themeName;
    private String date;
    private String time;
    private String reservedBy;
    private Integer participantCount;
    private String reservationId;
    private String reservationHistoryId;
    private HistoryType type;

    public ReservationHistoryTableDto(ReservationHistory entity) {
        this.reservationHistoryId = entity.getId();
        this.themeName = entity.getReservation().getThemeName();
        this.date = entity.getReservation().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = entity.getReservation().getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.reservedBy = entity.getReservedBy();
        this.participantCount = entity.getParticipantCount();
        this.reservationId = entity.getReservation().getId();
        this.type = entity.getType();
    }
}
