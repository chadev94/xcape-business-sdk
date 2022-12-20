package controller.response;

import domain.dto.ThemeDto;
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
    private Long price;
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
                dto.getMainImage(),
                dto.getBgImage(),
                dto.getPrice(),
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
