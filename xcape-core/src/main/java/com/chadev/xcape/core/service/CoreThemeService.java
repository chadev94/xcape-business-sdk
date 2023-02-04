package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.CoreThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreThemeService {

    private final CoreThemeRepository coreThemeRepository;
    private final CoreMerchantRepository coreMerchantRepository;
    private final DtoConverter dtoConverter;

    public ThemeDto getThemeById(Long themeId) {
        Theme theme = coreThemeRepository.findById(themeId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toThemeDto(theme);
    }

    public List<ThemeDto> getThemesByMerchantId(Long merchantId) {
        Merchant merchant = coreMerchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        return coreThemeRepository.findThemesByMerchant(merchant).stream().map(dtoConverter::toThemeDto).toList();
    }
}
