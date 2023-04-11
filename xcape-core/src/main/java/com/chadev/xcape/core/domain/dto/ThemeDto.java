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
    }

    @Override
    public String toString() {
        return "name : " + this.nameKo;
    }
}
