package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.chadev.xcape.core.repository.CoreThemeRepository;

@Service
@RequiredArgsConstructor
public class CoreThemeService {

    private final CoreThemeRepository coreThemeRepository;
    private final DtoConverter dtoConverter;

    public ThemeDto getThemeById(Long themeId) {
        Theme theme = coreThemeRepository.findById(themeId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toThemeDto(theme);
    }
}
