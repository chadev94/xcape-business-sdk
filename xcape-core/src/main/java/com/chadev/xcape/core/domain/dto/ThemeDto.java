package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private Boolean hasXKit;
    private Boolean isCrimeScene;
    private Boolean useYn;
    private List<PriceDto> priceList;
    private List<AbilityDto> abilityList;
    private List<ReservationDto> reservationList;

    public ThemeDto(Theme entity) {
        this.id = entity.getId();
        this.merchantId = entity.getMerchant().getId();
        this.nameKo = entity.getNameKo();
        this.nameEn = entity.getNameEn();
        this.mainImagePath = entity.getMainImagePath();
        this.bgImagePath = entity.getBgImagePath();
        this.timetable = entity.getTimetable();
        this.description = entity.getDescription();
        this.reasoning = entity.getReasoning();
        this.observation = entity.getObservation();
        this.activity = entity.getActivity();
        this.teamwork = entity.getTeamwork();
        this.minParticipantCount = entity.getMinParticipantCount();
        this.maxParticipantCount = entity.getMaxParticipantCount();
        this.difficulty = entity.getDifficulty();
        this.genre = entity.getGenre();
        this.point = entity.getPoint();
        this.youtubeLink = entity.getYoutubeLink();
        this.colorCode = entity.getColorCode();
        this.hasXKit = entity.getHasXKit();
        this.isCrimeScene = entity.getIsCrimeScene();
        this.useYn = entity.getUseYn();
        this.priceList = entity.getPriceList().stream().map(PriceDto::from).toList();
        this.abilityList = entity.getAbilityList().stream().map(AbilityDto::from).toList();
    }

    //    public ThemeDto(Theme theme) {
//        this.id = theme.getId();
//        this.nameKo = theme.getNameKo();
//        this.nameEn = theme.getNameEn();
//        this.merchantId = theme.getMerchant().getId();
//        this.mainImagePath = theme.getMainImagePath();
//        this.bgImagePath = theme.getBgImagePath();
//        this.timetable = theme.getTimetable();
//        this.description = theme.getDescription();
//        this.reasoning = theme.getReasoning();
//        this.observation = theme.getObservation();
//        this.activity = theme.getActivity();
//        this.teamwork = theme.getTeamwork();
//        this.minParticipantCount = theme.getMinParticipantCount();
//        this.maxParticipantCount = theme.getMaxParticipantCount();
//        this.difficulty = theme.getDifficulty();
//        this.genre = theme.getGenre();
//        this.point = theme.getPoint();
//        this.youtubeLink = theme.getYoutubeLink();
//        this.colorCode = theme.getColorCode();
//        this.hasXKit = theme.getHasXKit();
//        this.isCrimeScene = theme.getIsCrimeScene();
//    }

    @Override
    public String toString() {
        return "name : " + this.nameKo;
    }
}
