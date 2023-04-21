package com.chadev.xcape.api.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationCancelRequest {

    private String authenticationNumber;

    private String requestId;

    private String recipientNo;

}
