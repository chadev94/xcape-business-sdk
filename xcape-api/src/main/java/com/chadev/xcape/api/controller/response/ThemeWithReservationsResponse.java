package com.chadev.xcape.api.controller.response;

import com.chadev.xcape.core.domain.dto.PriceDto;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeWithReservationsResponse {

    private Long themeId;
    private String themeNameKo;
    private String themeNameEn;
    private String mainImagePath;
    private Integer minParticipantCount;
    private Integer maxParticipantCount;
    private Integer difficulty;
    private String colorCode;

    private List<PriceDto> priceList;

    private List<ReservationDto> reservationList;

    public static ThemeWithReservationsResponse from(ThemeDto themeDto, List<ReservationDto> reservationList) {
        return ThemeWithReservationsResponse.builder()
                .themeId(themeDto.getId())
                .themeNameKo(themeDto.getNameKo())
                .themeNameEn(themeDto.getNameEn())
                .mainImagePath(themeDto.getMainImagePath())
                .minParticipantCount(themeDto.getMinParticipantCount())
                .maxParticipantCount(themeDto.getMaxParticipantCount())
                .difficulty(themeDto.getDifficulty())
                .colorCode(themeDto.getColorCode())
                .priceList(themeDto.getPriceList())
                .reservationList(reservationList)
                .build();

    }
}
