package com.chadev.xcape.admin.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.TimetableDto;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.entity.Timetable;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.repository.ThemeRepository;
import com.chadev.xcape.core.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final ThemeRepository themeRepository;
    private final DtoConverter dtoConverter;

    public List<TimetableDto> getTimetableListByThemeId(Long themeId) {
        return timetableRepository.findTimetableListByThemeId(themeId).stream().map(dtoConverter::toTimetableDto).toList();
    }

    @Transactional
    public void modifyTimetableListByThemeId(List<TimetableDto> timetableDtoList, Long themeId) {
        Theme theme = themeRepository.findById(themeId).orElseThrow(IllegalArgumentException::new);
        saveTimetableList(timetableDtoList, theme);
    }

    @Transactional
    public void saveTimetableList(List<TimetableDto> timetableDtoList, Theme theme) {
        List<Timetable> timetableList = theme.getTimetableList();
        timetableDtoList.forEach(timetableDto -> {
            if (timetableDto.getId() == null) {
                timetableRepository.save(new Timetable(timetableDto, theme));
            } else if (timetableDto.getIsUsed()) {
                Timetable updateTimetable = timetableList.stream()
                        .filter(timetable -> Objects.equals(timetable.getId(), timetableDto.getId()))
                        .findFirst().orElseThrow(XcapeException::NOT_EXISTENT_TIMETABLE);
                updateTimetable.setTime(LocalTime.parse(timetableDto.getTime()));
            } else {
                Timetable deleteTimetable = timetableList.stream()
                        .filter(timetable -> Objects.equals(timetable.getId(), timetableDto.getId()))
                        .findFirst().orElseThrow(XcapeException::NOT_EXISTENT_TIMETABLE);
                timetableRepository.delete(deleteTimetable);
            }
        });
    }
}
