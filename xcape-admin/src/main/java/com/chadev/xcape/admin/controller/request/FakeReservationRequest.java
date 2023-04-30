package com.chadev.xcape.admin.controller.request;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FakeReservationRequest {
    List<String> reservationIdList;
    Long unreservedTime;
}
