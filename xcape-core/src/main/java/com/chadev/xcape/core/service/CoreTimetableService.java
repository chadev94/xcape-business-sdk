package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.TimetableDto;
import com.chadev.xcape.core.repository.CoreTimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreTimetableService {

    private final CoreTimetableRepository coreTimetableRepository;
    private final DtoConverter dtoConverter;

    public List<TimetableDto> getTimetableListByThemeId(Long themeId) {
        return coreTimetableRepository.findTimetableListByThemeId(themeId).stream().map(dtoConverter::toTimetableDto).toList();
    }
}
