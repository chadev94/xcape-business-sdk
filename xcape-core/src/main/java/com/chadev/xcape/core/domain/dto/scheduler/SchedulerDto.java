package com.chadev.xcape.core.domain.dto.scheduler;

import com.chadev.xcape.core.domain.entity.scheduler.Scheduler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchedulerDto {

    private Long id;

    private Long merchantId;

    private Boolean isAwake;

    private LocalTime time;

    public static SchedulerDto from(Scheduler entity) {
        return new SchedulerDto(
                entity.getMerchant().getId(),
                entity.getIsAwake(),
                entity.getTime()
        );
    }

    public SchedulerDto(Long merchantId, Boolean isAwake, LocalTime time) {
        this.merchantId = merchantId;
        this.isAwake = isAwake;
        this.time = time;
    }
}
