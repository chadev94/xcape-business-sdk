package com.chadev.xcape.core.domain.dto;

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

    private Integer participant;

    private Integer price;

    private Boolean isReserved;
}
