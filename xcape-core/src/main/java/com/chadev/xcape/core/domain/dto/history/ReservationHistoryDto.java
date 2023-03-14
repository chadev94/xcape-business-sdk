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

    private Long reservationId;

    private String info;

    private HistoryType type;

    private LocalDateTime dateTime;

    public ReservationHistoryDto(ReservationHistory entity){
        this.id = entity.getId();
        this.reservationId = entity.getReservation().getId();
        this.info = entity.getInfo();
        this.type = entity.getType();
        this.dateTime = entity.getDateTime();
    }
}
