package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "timetable")
public class Timetable {

    @Id
    @Column(name = "timetable_id")
    private Long id;

    @Setter
    @Column(name = "time")
    private LocalDateTime time;

    @Setter
    @Column(name = "type")
    private String type;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;
}
