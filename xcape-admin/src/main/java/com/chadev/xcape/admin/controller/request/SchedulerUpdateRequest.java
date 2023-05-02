package com.chadev.xcape.admin.controller.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SchedulerUpdateRequest {

    private Long merchantId;
    private Integer time;
}
