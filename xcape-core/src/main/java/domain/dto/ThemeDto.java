package domain.dto;

import controller.request.ThemeCreateRequest;
import controller.request.ThemeModifyRequest;
import domain.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link Theme} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ThemeDto {

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

    public static ThemeDto fromEntity(Theme entity) {
        return new ThemeDto(entity.getId(), entity.getMerchant().getId(), entity.getName(), entity.getMainImage(), entity.getBgImage(), entity.getPrice(), entity.getDescription(), entity.getReasoning(), entity.getObservation(), entity.getActivity(), entity.getTeamwork(), entity.getMinPersonnel(), entity.getMaxPersonnel(), entity.getDifficulty(), entity.getGenre(), entity.getPoint(), entity.getYoutubeLink(), entity.getColorCode(), entity.getHasXKit(), entity.getIsCrimeScene());
    }

    public static ThemeDto fromModifyRequest(ThemeModifyRequest request) {
        return new ThemeDto(request.getName(), request.getMainImage(), request.getBgImage(), request.getPrice(), request.getDescription(), request.getReasoning(), request.getObservation(), request.getActivity(), request.getTeamwork(), request.getMinPersonnel(), request.getMaxPersonnel(), request.getDifficulty(), request.getGenre(), request.getPoint(), request.getYoutubeLink(), request.getColorCode(), request.getHasXKit(), request.getIsCrimeScene());
    }

    public static ThemeDto fromCreateRequest(ThemeCreateRequest request) {
        return new ThemeDto(request.getName(), request.getMainImage(), request.getBgImage(), request.getPrice(), request.getDescription(), request.getReasoning(), request.getObservation(), request.getActivity(), request.getTeamwork(), request.getMinPersonnel(), request.getMaxPersonnel(), request.getDifficulty(), request.getGenre(), request.getPoint(), request.getYoutubeLink(), request.getColorCode(), request.getHasXKit(), request.getIsCrimeScene());
    }

    public ThemeDto(String name, String mainImage, String bgImage, Long price, String description, Integer reasoning, Integer observation, Integer activity, Integer teamwork, Integer minPersonnel, Integer maxPersonnel, Integer difficulty, String genre, String point, String youtubeLink, String colorCode, Boolean hasXKit, Boolean isCrimeScene) {
        this.name = name;
        this.mainImage = mainImage;
        this.bgImage = bgImage;
        this.price = price;
        this.description = description;
        this.reasoning = reasoning;
        this.observation = observation;
        this.activity = activity;
        this.teamwork = teamwork;
        this.minPersonnel = minPersonnel;
        this.maxPersonnel = maxPersonnel;
        this.difficulty = difficulty;
        this.genre = genre;
        this.point = point;
        this.youtubeLink = youtubeLink;
        this.colorCode = colorCode;
        this.hasXKit = hasXKit;
        this.isCrimeScene = isCrimeScene;
    }
}