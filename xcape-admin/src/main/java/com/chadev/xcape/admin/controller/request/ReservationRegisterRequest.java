package com.chadev.xcape.admin.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRegisterRequest {

    private String reservedBy;

    private String phoneNumber;

    private int participantCount;

    // general / openRoom
    private String roomType;

    private String authenticationNumber;
}
