package com.chadev.xcape.core.repository;

import com.chadev.xcape.core.domain.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findTimetableListByThemeId(Long themeId);
}
