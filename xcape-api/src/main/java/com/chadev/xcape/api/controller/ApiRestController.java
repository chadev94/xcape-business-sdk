package com.chadev.xcape.api.controller;

import com.chadev.xcape.api.controller.request.ReservationRegisterRequest;
import com.chadev.xcape.api.controller.response.ThemeWithReservationsResponse;
import com.chadev.xcape.api.service.ReservationService;
import com.chadev.xcape.api.util.kakao.KakaoSender;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.response.ErrorCode;
import com.chadev.xcape.core.response.Response;
import com.chadev.xcape.core.service.CoreMerchantService;
import com.chadev.xcape.core.service.CoreThemeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApiRestController {

    private final ReservationService reservationService;
    private final CoreThemeService coreThemeService;
    private final CoreMerchantService coreMerchantService;
    private final KakaoSender kakaoSender;

    //    admin module 과 중복 ---start
    @GetMapping("/merchants")
    public Response<List<MerchantDto>> getAllMerchantsWithThemes() {
        try {
            List<MerchantDto> merchantList = coreMerchantService.getAllMerchantsWithThemes();
            return Response.success(merchantList);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getAllMerchants", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @GetMapping("/merchants/{merchantId}")
    public Response<MerchantDto> getMerchantById(@PathVariable Long merchantId) {
        try {
            MerchantDto merchant = coreMerchantService.getMerchantWithAllInfo(merchantId);
            return Response.success(merchant);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getMerchantById", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @GetMapping("/themes/{themeId}")
    public Response<ThemeDto> getThemeById(@PathVariable Long themeId) {
        try {
            ThemeDto theme = coreThemeService.getThemeDetail(themeId);
            return Response.success(theme);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getTheme", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }
//    admin module 과 중복 ---end

    // 예약 페이지용 지점별 예약현황 조회
    @GetMapping("/merchants/{merchantId}/reservations")
    public Response<List<ThemeWithReservationsResponse>> getThemesWithReservations(
            @PathVariable Long merchantId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
            ) {
        List<ThemeDto> themeDtos = coreThemeService.getThemesByMerchantId(merchantId);
        List<ThemeWithReservationsResponse> response = new ArrayList<>();
        for (ThemeDto themeDto : themeDtos) {
            response.add(new ThemeWithReservationsResponse(
                    themeDto.getId(),
                    themeDto.getNameKo(),
                    themeDto.getNameEn(),
                    themeDto.getMainImagePath(),
                    themeDto.getMinParticipantCount(),
                    themeDto.getMaxParticipantCount(),
                    themeDto.getDifficulty(),
                    reservationService.getReservationsByThemeIdAndDate(themeDto.getId(), date)
            ));
        }

        return Response.success(response);
    }

    // 지점별 빈 예약 생성
    @PostMapping("/merchants/{merchantId}/reservations-batch")
    public Response<Void> createBatchReservations(
            @PathVariable Long merchantId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) throws IllegalArgumentException {
        reservationService.createEmptyReservationByMerchantId(merchantId, date);
        return Response.success();
    }

    // 예약 등록/수정
    @PutMapping("/reservations/{reservationId}")
    public Response<Long> registerReservation(@PathVariable Long reservationId, ReservationRegisterRequest request) {
        ReservationDto savedReservation = reservationService.registerReservationById(reservationId, request.getReservedBy(), request.getPhoneNumber(), request.getParticipantCount(), request.getRoomType());

        return Response.success(savedReservation.getId());
    }

    @GetMapping("/reservations/{reservationId}")
    public Response<ReservationDto> getReservation(@PathVariable Long reservationId) {
        ReservationDto reservationDto = reservationService.getReservation(reservationId);

        return Response.success(reservationDto);
    }

    // 예약 취소
    @PutMapping("/reservations/{reservationId}/cancel")
    public Response<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservationById(reservationId);
        return Response.success();
    }
}
