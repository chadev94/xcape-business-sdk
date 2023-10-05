package com.chadev.xcape.core.domain.dto.scheduler;

import com.chadev.xcape.core.domain.entity.scheduler.ClosedDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ClosedDateDto {

    private Long id;

    private Long merchantId;

    private LocalDate closedDate;

    public static ClosedDateDto from(ClosedDate entity) {
        return new ClosedDateDto(
                entity.getMerchant().getId(),
                entity.getClosedDate()
        );
    }

    public ClosedDateDto(Long merchantId, LocalDate closedDate) {
        this.merchantId = merchantId;
        this.closedDate = closedDate;
    }
}
