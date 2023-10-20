package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Hint;
import lombok.*;

/**
 * A DTO for the {@link Hint} entity
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HintDto {

    private Long id;
    private Long themeId;
    private String code;
    private String message1;
    private String message2;
    private boolean isUsed;
    private String registeredBy;
    private String modifiedBy;

    public HintDto(Hint entity) {
        this.id = entity.getId();
        this.code = entity.getCode();
        this.message1 = entity.getMessage1();
        this.message2 = entity.getMessage2();
        this.isUsed = entity.isUsed();
        this.registeredBy = entity.getRegisteredBy();
        this.modifiedBy = entity.getModifiedBy();
        this.themeId = entity.getTheme().getId();
    }
}

