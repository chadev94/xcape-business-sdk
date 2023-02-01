package com.chadev.xcape.core.domain.dto;

import com.chadev.xcape.core.domain.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String generalPrice;
    private String openRoomPrice;
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

//    public static ThemeDto toDto(Theme entity) {
//        return new ThemeDto(entity.getId(), entity.getMerchant().getId(), entity.getName(), entity.getMainImage(), entity.getBgImage(), entity.getPrice(), entity.getDescription(), entity.getReasoning(), entity.getObservation(), entity.getActivity(), entity.getTeamwork(), entity.getMinPersonnel(), entity.getMaxPersonnel(), entity.getDifficulty(), entity.getGenre(), entity.getPoint(), entity.getYoutubeLink(), entity.getColorCode(), entity.getHasXKit(), entity.getIsCrimeScene());
//    }

//    public static ThemeDto fromModifyRequest(ThemeModifyRequest request) {
//        return new ThemeDto(request.getName(), request.getMainImage(), request.getBgImage(), request.getPrice(), request.getDescription(), request.getReasoning(), request.getObservation(), request.getActivity(), request.getTeamwork(), request.getMinPersonnel(), request.getMaxPersonnel(), request.getDifficulty(), request.getGenre(), request.getPoint(), request.getYoutubeLink(), request.getColorCode(), request.getHasXKit(), request.getIsCrimeScene());
//    }
//
//    public static ThemeDto fromCreateRequest(ThemeCreateRequest request) {
//        return new ThemeDto(request.getName(), request.getMainImage(), request.getBgImage(), request.getPrice(), request.getDescription(), request.getReasoning(), request.getObservation(), request.getActivity(), request.getTeamwork(), request.getMinPersonnel(), request.getMaxPersonnel(), request.getDifficulty(), request.getGenre(), request.getPoint(), request.getYoutubeLink(), request.getColorCode(), request.getHasXKit(), request.getIsCrimeScene());
//    }


    private ThemeDto(String nameKo, String nameEn, String mainImagePath, String bgImagePath, String generalPrice, String openRoomPrice, String timetable, String description, Integer reasoning, Integer observation, Integer activity, Integer teamwork, Integer minParticipantCount, Integer maxParticipantCount, Integer difficulty, String genre, String point, String youtubeLink, String colorCode, Character hasXKit, Character isCrimeScene) {
        this.nameKo = nameKo;
        this.nameEn = nameEn;
        this.mainImagePath = mainImagePath;
        this.bgImagePath = bgImagePath;
        this.generalPrice = generalPrice;
        this.openRoomPrice = openRoomPrice;
        this.timetable = timetable;
        this.description = description;
        this.reasoning = reasoning;
        this.observation = observation;
        this.activity = activity;
        this.teamwork = teamwork;
        this.minParticipantCount = minParticipantCount;
        this.maxParticipantCount = maxParticipantCount;
        this.difficulty = difficulty;
        this.genre = genre;
        this.point = point;
        this.youtubeLink = youtubeLink;
        this.colorCode = colorCode;
        this.hasXKit = hasXKit;
        this.isCrimeScene = isCrimeScene;
    }

    public ThemeDto(Theme theme) {
        this.id = theme.getId();
        this.nameKo = theme.getNameKo();
        this.nameEn = theme.getNameEn();
        this.merchantId = theme.getMerchant().getId();
        this.mainImagePath = theme.getMainImagePath();
        this.bgImagePath = theme.getBgImagePath();
        this.generalPrice = theme.getGeneralPrice();
        this.openRoomPrice = theme.getOpenRoomPrice();
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
