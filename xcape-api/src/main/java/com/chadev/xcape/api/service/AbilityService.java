package com.chadev.xcape.api.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.AbilityDto;
import com.chadev.xcape.core.repository.AbilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbilityService {

    private final AbilityRepository abilityRepository;
    private final DtoConverter dtoConverter;

    public List<AbilityDto> getAllAbilityList() {
        return abilityRepository.findAll().stream().map(dtoConverter::toAbilityDto).toList();
    }

    public List<AbilityDto> getAbilityListByThemeId(Long themeId) {
        return abilityRepository.findAbilityListByThemeId(themeId).stream().map(dtoConverter::toAbilityDto).toList();
    }
}
