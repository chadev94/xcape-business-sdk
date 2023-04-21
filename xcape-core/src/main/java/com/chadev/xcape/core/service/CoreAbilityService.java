package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.AbilityDto;
import com.chadev.xcape.core.domain.entity.Ability;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.repository.CoreAbilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CoreAbilityService {

    private final CoreAbilityRepository coreAbilityRepository;
    private final DtoConverter dtoConverter;

    public List<AbilityDto> getAbilityListByThemeId(Long themeId) {
        return coreAbilityRepository.findAbilityListByThemeId(themeId).stream().map(dtoConverter::toAbilityDto).toList();
    }

    public List<AbilityDto> getAbilityListByMerchantId(Long merchantId) {
        return coreAbilityRepository.findAbilityListByMerchantId(merchantId).stream().map(dtoConverter::toAbilityDto).toList();
    }

    @Transactional
    public void saveAbilityList(List<AbilityDto> abilityDtoList, Theme theme) {
        List<Ability> abilityList = theme.getAbilityList();
        abilityDtoList.forEach(abilityDto -> {
            if (abilityDto.getId() == null) {
                coreAbilityRepository.save(new Ability(abilityDto, theme.getMerchant(), theme));
            } else {
                Ability updateAbility = abilityList.stream()
                        .filter(ability -> Objects.equals(ability.getId(), abilityDto.getId()))
                        .findFirst()
                        .orElseThrow(IllegalArgumentException::new);
                updateAbility.setCode(abilityDto.getCode());
                updateAbility.setName(abilityDto.getName());
                updateAbility.setValue(abilityDto.getValue());
            }
        });
    }

    public List<AbilityDto> getAllAbilityList() {
        return coreAbilityRepository.findAll().stream().map(dtoConverter::toAbilityDto).toList();
    }
}
