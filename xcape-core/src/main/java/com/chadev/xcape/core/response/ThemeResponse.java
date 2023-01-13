package com.chadev.xcape.core.response;

import com.chadev.xcape.core.domain.dto.ThemeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ThemeResponse {

    private Long id;
    private Long merchantId;
    private String name;
    private String mainImage;
    private String bgImage;
    private String price;
    private String description;
    private Integer reasoning;
    private Integer observation;
    private Integer activity;
    private Integer teamwork;
    private Integer minPersonnel;
    private Integer maxPersonnel;
    private Integer difficulty;
    private String genre;
    private String point;
    private String youtubeLink;
    private String colorCode;
    private Boolean hasXKit;
    private Boolean isCrimeScene;

    public static ThemeResponse fromDto(ThemeDto dto) {
        return new ThemeResponse(
                dto.getId(),
                dto.getMerchantId(),
                dto.getName(),
                dto.getMainImagePath(),
                dto.getBgImagePath(),
                dto.getGeneralPrice(),
                dto.getDescription(),
                dto.getReasoning(),
                dto.getObservation(),
                dto.getActivity(),
                dto.getTeamwork(),
                dto.getMinPersonnel(),
                dto.getMaxPersonnel(),
                dto.getDifficulty(),
                dto.getGenre(),
                dto.getPoint(),
                dto.getYoutubeLink(),
                dto.getColorCode(),
                dto.getHasXKit(),
                dto.getIsCrimeScene()
        );
    }
}
