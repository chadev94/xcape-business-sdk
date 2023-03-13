package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Ability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbilityDto {

    private Long id;
    private String code;
    private String name;
    private Integer value;
    private Long merchantId;
    private Long themeId;

    public AbilityDto(Ability entity) {
        this.id = entity.getId();
        this.code = entity.getCode();
        this.name = entity.getName();
        this.value = entity.getValue();
        this.merchantId = entity.getMerchant().getId();
        this.themeId = entity.getTheme().getId();
    }
}
