package com.chadev.xcape.core.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Table(name = "theme", indexes = {
        @Index(columnList = "id"),
        @Index(columnList = "theme_name")
})
@Entity
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    Merchant merchant;

    //    테마 이름
    @Setter @Column(name = "theme_name", nullable = false)
    private String name;

    //    테마 메인 이미지
    @Setter @Column(name = "main_image", nullable = false)
    private String mainImage;

    //    소개페이지의 배경이미지
    @Setter @Column(name = "bg_image", nullable = false)
    private String bgImage;

    //    가격
    @Setter @Column(name = "price", nullable = false)
    private Long price;

    //    소개
    @Setter @Column(name = "description", nullable = false)
    private String description;

    //    추리력
    @Setter @Column(name = "reasoning", nullable = false)
    private Integer reasoning;

    //    관찰력
    @Setter @Column(name = "observation", nullable = false)
    private Integer observation;

    //    활동성
    @Setter @Column(name = "activity", nullable = false)
    private Integer activity;

    //    팀워크
    @Setter @Column(name = "teamwork", nullable = false)
    private Integer teamwork;

    //   최소 인원
    @Setter @Column(name = "min_personnel", nullable = false)
    private Integer minPersonnel;

    //    최대 인원
    @Setter @Column(name = "max_personnel", nullable = false)
    private Integer maxPersonnel;

    //    난이도
    @Setter @Column(name = "difficulty", nullable = false)
    private Integer difficulty;

    //    장르
    @Setter @Column(name = "genre", nullable = false)
    private String genre;

    //    Point(json list)
    @Setter @Column(name = "point", nullable = false)
    private String point;

    //    youtube 링크
    @Setter @Column(name = "youtube_link", nullable = false)
    private String youtubeLink;

    //    hex code(컬러)
    @Setter @Column(name = "color_code", nullable = false)
    private String colorCode;

    //    Use x-kit
    @Setter @Column(name = "has_x_kit", nullable = false)
    private Boolean hasXKit;

    //    크라임씬(y/n)
    @Setter @Column(name = "is_crime_scene", nullable = false)
    private Boolean isCrimeScene;

    //    id 제외 모든 파라미터 받는 생성자
    private Theme(Merchant merchant, String name, String mainImage, String bgImage, Long price, String description, Integer reasoning, Integer observation, Integer activity, Integer teamwork, Integer minPersonnel, Integer maxPersonnel, Integer difficulty, String genre, String point, String youtubeLink, String colorCode, Boolean hasXKit, Boolean isCrimeScene) {this.merchant = merchant;this.name = name;this.mainImage = mainImage;this.bgImage = bgImage;this.price = price;this.description = description;this.reasoning = reasoning;this.observation = observation;this.activity = activity;this.teamwork = teamwork;this.minPersonnel = minPersonnel;this.maxPersonnel = maxPersonnel;this.difficulty = difficulty;this.genre = genre;this.point = point;this.youtubeLink = youtubeLink;this.colorCode = colorCode;this.hasXKit = hasXKit;this.isCrimeScene = isCrimeScene;}

    //    팩토리 메소드
    public static Theme of(Merchant merchant, String name, String mainImage, String bgImage, Long price, String desc, Integer reasoning, Integer observation, Integer activity, Integer teamwork, Integer minPersonnel, Integer maxPersonnel, Integer difficulty, String genre, String point, String youtubeLink, String colorCode, Boolean hasXKit, Boolean isCrimeScene) {
        return new Theme(merchant, name, mainImage, bgImage, price, desc, reasoning, observation, activity, teamwork, minPersonnel, maxPersonnel, difficulty, genre, point, youtubeLink, colorCode, hasXKit, isCrimeScene);
    }
}
