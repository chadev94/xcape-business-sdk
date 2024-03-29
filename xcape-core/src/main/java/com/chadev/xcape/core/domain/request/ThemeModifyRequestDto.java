package com.chadev.xcape.core.domain.request;

import com.chadev.xcape.core.domain.dto.AbilityDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeModifyRequestDto {

    @NotNull
    private Long themeId;

    @NotNull
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
    private Integer runningTime;
    private List<AbilityDto> abilityList;
}
