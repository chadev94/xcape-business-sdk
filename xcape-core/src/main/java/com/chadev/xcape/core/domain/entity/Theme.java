package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Builder
@Table(name = "theme")
@AllArgsConstructor
public class Theme extends AuditingFields {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    Merchant merchant;

    //    테마 이름
    @Column(name = "theme_name_ko")
    private String nameKo;

    @Column(name = "theme_name_en")
    private String nameEn;

    //    테마 메인 이미지
    @Column(name = "main_image_path", length = 500)
    private String mainImagePath;

    //    소개페이지의 배경이미지
    @Column(name = "bg_image_path", length = 500)
    private String bgImagePath;

    //    소개
    @Column(name = "description")
    private String description;

    //   최소 인원
    @Column(name = "min_participant_count")
    private Integer minParticipantCount;

    //    최대 인원
    @Column(name = "max_participant_count")
    private Integer maxParticipantCount;

    //    난이도
    @Column(name = "difficulty")
    private Integer difficulty;

    //    장르
    @Column(name = "genre")
    private String genre;

    //    Point(json list)
    @Column(name = "point")
    private String point;

    //    youtube 링크
    @Column(name = "youtube_link")
    private String youtubeLink;

    //    hex code(컬러)
    @Column(name = "color_code")
    private String colorCode;

    //    Use x-kit
    @Column(name = "has_x_kit")
    private Boolean hasXKit;

    //    크라임씬(y/n)
    @Column(name = "is_crime_scene")
    private Boolean isCrimeScene;

    //
    @Column(name = "use_yn", length = 1)
    private Boolean useYn;

    //  소요시간
    @Column(name = "running_time")
    private Integer runningTime;

    @OneToMany(mappedBy = "theme")
    private List<Timetable> timetableList;
    
    @OneToMany(mappedBy = "theme")
    private List<Price> priceList;

    @OneToMany(mappedBy = "theme")
    private List<Ability> abilityList;
}
