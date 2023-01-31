package com.chadev.xcape.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationDto {

    private Long id;

    private Long themeId;

    private Long merchantId;

    private String startTime;

    private String date;

    private String reservedBy;

    private String phoneNumber;

    private Integer count;

    private Boolean isReserved;
}
