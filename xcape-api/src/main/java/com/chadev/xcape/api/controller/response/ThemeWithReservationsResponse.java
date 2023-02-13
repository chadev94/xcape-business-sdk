package com.chadev.xcape.api.controller.response;

import com.chadev.xcape.core.domain.entity.Reservation;
import com.chadev.xcape.core.repository.mapping.ReservationInfo;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * {
 *   "resultCode": "SUCCESS",
 *   "resultMessage": null,
 *   "result": {
 *     "themeId": 1,
 *     "themeName": "기억의 틈",
 *     "mainImagePath": "https://xcape-business-sdk-uploads.s3.ap-northeast-2.amazonaws.com/1/2dec5f78-7d2b-4cf6-8544-45c866cd1dcc_icon.png",
 * 		"minParticipant": 2
 *     "maxParticipant": 6
 *     "difficulty": 3,
 * 		"reservation": [{
 * 	    "id": 1,
 * 			"reservation_time": "09:00",
 * 	    "isReserved": "N"
 *            },* 		{
 * 	    "id": 2,
 * 			"reservation_time": "10:00",
 * 	    "isReserved": "Y"* 	  }        ,
 * 		{
 * 	    "id": 3,
 * 			"reservation_time": "11:00",
 * 	    "isReserve      "N"
 *    }]
 * 	}
 * }
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeWithReservationsResponse {

    private Long themeId;
    private String themeNameKo;
    private String themeNameEn;
    private String mainImagePath;
    private Integer minParticipant;
    private Integer maxParticipant;
    private Integer difficulty;
    /**
     * ReservationDto : {
     *     "id": 3,
     *     "reservation_time": "11:00",
     *     "isReserved: "N"
     * }
     */
    private List<ReservationInfo> reservationInfos;

    public static ThemeWithReservationsResponse toThemeWithReservationsResponse(ThemeDto themeDto, List<ReservationInfo> reservationInfos) {
        return new ThemeWithReservationsResponse(
                themeDto.getId(),
                themeDto.getNameKo(),
                themeDto.getNameEn(),
                themeDto.getMainImagePath(),
                themeDto.getMinParticipantCount(),
                themeDto.getMaxParticipantCount(),
                themeDto.getDifficulty(),
                reservationInfos
        );
    }
}
