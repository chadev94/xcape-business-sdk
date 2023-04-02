package com.chadev.xcape.admin.controller.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchedulerResponse {

    private String merchantName;
    List<LocalDate> closedDateList;

}
