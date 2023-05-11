package com.chadev.xcape.core.domain.entity;

import com.chadev.xcape.core.domain.dto.TimetableDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "timetable")
public class Timetable {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Long id;

    @Setter
    @Column(name = "time")
    private LocalTime time;

    @Setter
    @Column(name = "type")
    private String type;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public Timetable(TimetableDto timetableDto, Theme theme) {
        this.time = LocalTime.parse(timetableDto.getTime());
        this.theme = theme;
    }
}
