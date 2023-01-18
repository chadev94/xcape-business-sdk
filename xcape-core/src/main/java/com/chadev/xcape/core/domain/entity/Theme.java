package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Entity
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
    @Setter @Column(name = "theme_name", nullable = true)
    private String name;

    //    테마 메인 이미지
    @Setter @Column(name = "main_image_path", nullable = true)
    private String mainImagePath;

    //    소개페이지의 배경이미지
    @Setter @Column(name = "bg_image_path", nullable = true)
    private String bgImagePath;

    //    가격
    @Setter @Column(name = "general_price", nullable = true)
    private String generalPrice;

    @Setter @Column(name = "open_room_price", nullable = true)
    private String openRoomPrice;

    @Setter @Column(name = "timetable", nullable = true)
    private String timetable;
    //    소개
    @Setter @Column(name = "description", nullable = true)
    private String description;

    //    추리력
    @Setter @Column(name = "reasoning", nullable = true)
    private Integer reasoning;

    //    관찰력
    @Setter @Column(name = "observation", nullable = true)
    private Integer observation;

    //    활동성
    @Setter @Column(name = "activity", nullable = true)
    private Integer activity;

    //    팀워크
    @Setter @Column(name = "teamwork", nullable = true)
    private Integer teamwork;

    //   최소 인원
    @Setter @Column(name = "min_personnel", nullable = true)
    private Integer minPersonnel;

    //    최대 인원
    @Setter @Column(name = "max_personnel", nullable = true)
    private Integer maxPersonnel;

    //    난이도
    @Setter @Column(name = "difficulty", nullable = true)
    private Integer difficulty;

    //    장르
    @Setter @Column(name = "genre", nullable = true)
    private String genre;

    //    Point(json list)
    @Setter @Column(name = "point", nullable = true)
    private String point;

    //    youtube 링크
    @Setter @Column(name = "youtube_link", nullable = true)
    private String youtubeLink;

    //    hex code(컬러)
    @Setter @Column(name = "color_code", nullable = true)
    private String colorCode;

    //    Use x-kit
    @Setter @Column(name = "has_x_kit", nullable = true)
    private Boolean hasXKit;

    //    크라임씬(y/n)
    @Setter @Column(name = "is_crime_scene", nullable = true)
    private Boolean isCrimeScene;

    @Setter @Column(name = "is_used", length = 1)
    private Character isUsed;

    //    id 제외 모든 파라미터 받는 생성자
    private Theme(Merchant merchant, String name, String mainImagePath, String bgImagePath, String generalPrice, String openRoomPrice, String description, Integer reasoning, Integer observation, Integer activity, Integer teamwork, Integer minPersonnel, Integer maxPersonnel, Integer difficulty, String genre, String point, String youtubeLink, String colorCode, Boolean hasXKit, Boolean isCrimeScene) {this.merchant = merchant;this.name = name;this.mainImagePath = mainImagePath;this.bgImagePath = bgImagePath;this.generalPrice = generalPrice;this.openRoomPrice = openRoomPrice;this.description = description;this.reasoning = reasoning;this.observation = observation;this.activity = activity;this.teamwork = teamwork;this.minPersonnel = minPersonnel;this.maxPersonnel = maxPersonnel;this.difficulty = difficulty;this.genre = genre;this.point = point;this.youtubeLink = youtubeLink;this.colorCode = colorCode;this.hasXKit = hasXKit;this.isCrimeScene = isCrimeScene;}

    //    팩토리 메소드
    @Builder
    public static Theme of(Merchant merchant, String name, String mainImagePath, String bgImagePath, String generalPrice, String openRoomPrice, String description, Integer reasoning, Integer observation, Integer activity, Integer teamwork, Integer minPersonnel, Integer maxPersonnel, Integer difficulty, String genre, String point, String youtubeLink, String colorCode, Boolean hasXKit, Boolean isCrimeScene) {
        return new Theme(merchant, name, mainImagePath, bgImagePath, generalPrice, openRoomPrice, description, reasoning, observation, activity, teamwork, minPersonnel, maxPersonnel, difficulty, genre, point, youtubeLink, colorCode, hasXKit, isCrimeScene);
    }
}
