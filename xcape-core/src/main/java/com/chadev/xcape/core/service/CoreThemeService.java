package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.repository.CoreThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@EnableCaching
@Service
@RequiredArgsConstructor
public class CoreThemeService {

    private final CoreThemeRepository coreThemeRepository;
    private final CorePriceService corePriceService;
    private final CoreAbilityService coreAbilityService;
    private final DtoConverter dtoConverter;

    public ThemeDto getThemeById(Long themeId) {
        Theme theme = coreThemeRepository.findById(themeId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toThemeDto(theme);
    }

    public List<ThemeDto> getThemesByMerchantId(Long merchantId) {
        return coreThemeRepository.findThemesByMerchantId(merchantId).stream().map(dtoConverter::themeDtoWithPriceListAndAbilityList).toList();
    }

    public ThemeDto getThemeDetail(Long themeId) {
        ThemeDto theme = getThemeById(themeId);
        theme.setAbilityList(coreAbilityService.getAbilityListByThemeId(themeId));
        return theme;
    }
}
