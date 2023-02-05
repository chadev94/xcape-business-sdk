package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationDto {

    private Long id;

    private Long themeId;

    private Long merchantId;

    private LocalDate date;

    private LocalTime time;

    private String reservedBy;

    private String phoneNumber;

    private Integer participantCount;

    private Integer price;

    private Boolean isReserved;

    public ReservationDto(Reservation entity) {
        this.id = entity.getId();
        this.themeId = entity.getTheme().getId();
        this.merchantId = entity.getMerchant().getId();
        this.date = entity.getDate();
        this.time = entity.getTime();
        this.reservedBy = entity.getReservedBy();
        this.phoneNumber = entity.getPhoneNumber();
        this.participantCount = entity.getParticipantCount();
        this.price = entity.getPrice();
        this.isReserved = entity.getIsReserved();
    }
}
