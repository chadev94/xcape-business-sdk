package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.PriceDto;
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
    private final CoreAbilityService coreAbilityService;
    private final DtoConverter dtoConverter;

    public ThemeDto getThemeById(Long themeId) {
        Theme theme = coreThemeRepository.findById(themeId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toThemeDto(theme);
    }

    public List<ThemeDto> getThemeIdListByMerchantId(Long merchantId) {
        List<Theme> themeList = coreThemeRepository.findThemesWithPriceListByMerchantId(merchantId);
        return themeList.stream().map(theme -> {
            ThemeDto themeDto = dtoConverter.toThemeDto(theme);
            List<PriceDto> priceDtoList = theme.getPriceList().stream().map(dtoConverter::toPriceDto).toList();
            themeDto.setPriceList(priceDtoList);
            return themeDto;
        }).toList();
    }

    public ThemeDto getThemeDetail(Long themeId) {
        ThemeDto theme = getThemeById(themeId);
        theme.setAbilityList(coreAbilityService.getAbilityListByThemeId(themeId));
        return theme;
    }

    public List<ThemeDto> getAllThemeList() {
        return coreThemeRepository.findAll().stream().map(dtoConverter::toThemeDto).toList();
    }

    public List<ThemeDto> getThemeListByMerchantId(Long merchantId) {
        return coreThemeRepository.findThemesByMerchantId(merchantId).stream().map(dtoConverter::toThemeDto).toList();
    }
}
