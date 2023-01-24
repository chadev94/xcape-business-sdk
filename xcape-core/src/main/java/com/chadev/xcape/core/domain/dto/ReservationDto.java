package com.chadev.xcape.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationDto {

    private Long id;

    private Long themeId;

    private LocalDateTime startTime;

    private String name;

    private String phoneNumber;

    private Integer headCount;

    private Integer price;

    private Boolean isReserved;
}
