package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link Theme} entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto {

    private Long id;
    private Long merchantId;
    private String nameKo;
    private String nameEn;
    private String mainImagePath;
    private String bgImagePath;
    private List<PriceDto> priceList;
    private String timetable;
    private String description;
    private Integer reasoning;
    private Integer observation;
    private Integer activity;
    private Integer teamwork;
    private Integer minParticipantCount;
    private Integer maxParticipantCount;
    private Integer difficulty;
    private String genre;
    private String point;
    private String youtubeLink;
    private String colorCode;
    private Character hasXKit;
    private Character isCrimeScene;

    public ThemeDto(Theme theme) {
        this.id = theme.getId();
        this.nameKo = theme.getNameKo();
        this.nameEn = theme.getNameEn();
        this.merchantId = theme.getMerchant().getId();
        this.mainImagePath = theme.getMainImagePath();
        this.priceList = theme.getPriceList().stream().map(PriceDto::new).collect(Collectors.toList());
        this.bgImagePath = theme.getBgImagePath();
        this.timetable = theme.getTimetable();
        this.description = theme.getDescription();
        this.reasoning = theme.getReasoning();
        this.observation = theme.getObservation();
        this.activity = theme.getActivity();
        this.teamwork = theme.getTeamwork();
        this.minParticipantCount = theme.getMinParticipantCount();
        this.maxParticipantCount = theme.getMaxParticipantCount();
        this.difficulty = theme.getDifficulty();
        this.genre = theme.getGenre();
        this.point = theme.getPoint();
        this.youtubeLink = theme.getYoutubeLink();
        this.colorCode = theme.getColorCode();
        this.hasXKit = theme.getHasXKit();
        this.isCrimeScene = theme.getIsCrimeScene();
    }

    @Override
    public String toString() {
        return "name : " + this.nameKo;
    }
}
