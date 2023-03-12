package com.chadev.xcape.admin.controller.response;

import com.chadev.xcape.core.domain.dto.AbilityDto;
import com.chadev.xcape.core.domain.dto.PriceDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ThemeDetailResponseDto {
    ThemeDto theme;
    List<PriceDto> priceList;
    List<AbilityDto> abilityList;
}
