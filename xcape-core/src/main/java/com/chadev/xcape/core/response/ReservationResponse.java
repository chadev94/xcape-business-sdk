package com.chadev.xcape.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private String themeName;
    private String reservedBy;
    private String phoneNumber;
    private Integer participantCount;
    private LocalDateTime reservationTime;  //  예약 시간
    private LocalDateTime reservedAt;   //  등록 시간
    private Integer price;
}
