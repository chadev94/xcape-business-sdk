package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    Merchant merchant;

    //    테마 이름
    @Setter
    @Column(name = "theme_name_ko")
    private String nameKo;

    @Setter
    @Column(name = "theme_name_en")
    private String nameEn;

    //    테마 메인 이미지
    @Setter
    @Column(name = "main_image_path", length = 500)
    private String mainImagePath;

    //    소개페이지의 배경이미지
    @Setter
    @Column(name = "bg_image_path", length = 500)
    private String bgImagePath;

    @Setter
    @Column(name = "timetable")
    private String timetable;
    //    소개
    @Setter
    @Column(name = "description")
    private String description;

    //   최소 인원
    @Setter
    @Column(name = "min_participant_count")
    private Integer minParticipantCount;

    //    최대 인원
    @Setter
    @Column(name = "max_participant_count")
    private Integer maxParticipantCount;

    //    난이도
    @Setter
    @Column(name = "difficulty")
    private Integer difficulty;

    //    장르
    @Setter
    @Column(name = "genre")
    private String genre;

    //    Point(json list)
    @Setter
    @Column(name = "point")
    private String point;

    //    youtube 링크
    @Setter
    @Column(name = "youtube_link")
    private String youtubeLink;

    //    hex code(컬러)
    @Setter
    @Column(name = "color_code")
    private String colorCode;

    //    Use x-kit
    @Setter
    @Column(name = "has_x_kit")
    private Boolean hasXKit;

    //    크라임씬(y/n)
    @Setter
    @Column(name = "is_crime_scene")
    private Boolean isCrimeScene;

    @Setter
    @Column(name = "use_yn", length = 1)
    private Boolean useYn;

    @Setter
    @OneToMany(mappedBy = "theme")
    private List<Price> priceList = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "theme")
    private List<Ability> abilityList = new ArrayList<>();

    //    id 제외 모든 파라미터 받는 생성자
    public Theme(Merchant merchant, String nameKo, String nameEn, String mainImagePath, String bgImagePath, String timetable, String description, Integer minParticipantCount, Integer maxParticipantCount, Integer difficulty, String genre, String point, String youtubeLink, String colorCode, Boolean hasXKit, Boolean isCrimeScene, Boolean useYn, List<Price> priceList, List<Ability> abilityList) {
        this.merchant = merchant;
        this.nameKo = nameKo;
        this.nameEn = nameEn;
        this.mainImagePath = mainImagePath;
        this.bgImagePath = bgImagePath;
        this.timetable = timetable;
        this.description = description;
        this.minParticipantCount = minParticipantCount;
        this.maxParticipantCount = maxParticipantCount;
        this.difficulty = difficulty;
        this.genre = genre;
        this.point = point;
        this.youtubeLink = youtubeLink;
        this.colorCode = colorCode;
        this.hasXKit = hasXKit;
        this.isCrimeScene = isCrimeScene;
        this.useYn = useYn;
        this.priceList = priceList;
        this.abilityList = abilityList;
    }

    //    팩토리 메소드
    @Builder
    public static Theme of(Merchant merchant, String nameKo, String nameEn, String mainImagePath, String bgImagePath, String timetable, String description, Integer minParticipantCount, Integer maxParticipantCount, Integer difficulty, String genre, String point, String youtubeLink, String colorCode, Boolean hasXKit, Boolean isCrimeScene, Boolean useYn, List<Price> priceList, List<Ability> abilityList) {
        return new Theme(merchant, nameKo, nameEn, mainImagePath, bgImagePath, timetable, description, minParticipantCount, maxParticipantCount, difficulty, genre, point, youtubeLink, colorCode, hasXKit, isCrimeScene, useYn, priceList, abilityList);
    }
}
