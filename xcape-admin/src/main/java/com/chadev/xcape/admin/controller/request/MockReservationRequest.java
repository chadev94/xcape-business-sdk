package com.chadev.xcape.admin.controller.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MockReservationRequest {

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long merchantId;
    private Long themeId;
}
