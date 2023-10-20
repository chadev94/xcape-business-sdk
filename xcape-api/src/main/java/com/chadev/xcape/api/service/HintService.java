package com.chadev.xcape.api.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.HintDto;
import com.chadev.xcape.core.repository.HintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HintService {

    private final HintRepository hintRepository;
    private final DtoConverter dtoConverter;

    public List<HintDto> getHintListByThemeId(Long themeId) {
        return hintRepository.findHintsByThemeId(themeId).stream().map(dtoConverter::toHintDto).toList();
    }

}
