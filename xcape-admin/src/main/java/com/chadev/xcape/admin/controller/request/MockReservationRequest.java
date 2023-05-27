package com.chadev.xcape.admin.controller.request;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MockReservationRequest {
    List<String> reservationIdList;
    Long unreservedTime;
}
