package com.chadev.xcape.api.controller;

import com.chadev.xcape.api.controller.request.ReservationRegisterRequest;
import com.chadev.xcape.api.controller.response.ReservationWithReservationHistoryResponse;
import com.chadev.xcape.api.controller.response.ThemeWithReservationsResponse;
import com.chadev.xcape.api.service.BannerService;
import com.chadev.xcape.api.service.ReservationService;
import com.chadev.xcape.core.domain.dto.BannerDto;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
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
    private final BannerService bannerService;

    //    admin module 과 중복 ---start
    @GetMapping("/merchants")
    public Response<List<MerchantDto>> getAllMerchantsWithThemes() {
        try {
            List<MerchantDto> merchantList = coreMerchantService.getAllMerchantList();
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
    @GetMapping(value = "/reservations", params = {"merchantId", "date"})
    public Response<List<ThemeWithReservationsResponse>> getThemesWithReservations(
            Long merchantId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        List<ThemeDto> themeDtoList = coreThemeService.getThemesByMerchantId(merchantId);
        List<ThemeWithReservationsResponse> response = new ArrayList<>();
        for (ThemeDto themeDto : themeDtoList) {
            response.add(ThemeWithReservationsResponse.from(themeDto, reservationService.getReservationsByThemeIdAndDate(themeDto.getId(), date)));
        }

        return Response.success(response);
    }

    // 예약 등록/수정
    @PutMapping("/reservations/{reservationId}")
    public Response<ReservationDto> registerReservation(@PathVariable Long reservationId, ReservationRegisterRequest request) {
        ReservationDto savedReservation = reservationService.registerReservationById(reservationId, request.getReservedBy(), request.getPhoneNumber(), request.getParticipantCount(), request.getRoomType());

        return Response.success(savedReservation);
    }

    @GetMapping("/reservations/{reservationId}")
    public Response<ReservationDto> getReservation(@PathVariable Long reservationId) {
        ReservationDto reservationDto = reservationService.getReservation(reservationId);

        return Response.success(reservationDto);
    }

    // 예약 취소
    @DeleteMapping("/reservations/{reservationId}")
    public Response<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservationById(reservationId);
        return Response.success();
    }

    // 연락처로 예약 이력 조회
    @GetMapping(value = "/reservations", params = {"phoneNumber"})
    public Response<List<ReservationWithReservationHistoryResponse>> getReservations(String phoneNumber) {
        List<ReservationWithReservationHistoryResponse> response = new ArrayList<>();
        List<ReservationHistoryDto> reservationHistories = reservationService.getReservationHistories(phoneNumber);
        for (ReservationHistoryDto reservationHistory : reservationHistories) {
            response.add(ReservationWithReservationHistoryResponse.from(reservationService.getReservation(reservationHistory.getReservationId()), reservationHistory));
        }

        return Response.success(response);
    }

    @GetMapping("/themes")
    public Response<List<ThemeDto>> getAllThemeList() {
        try {
            List<ThemeDto> themeList = coreThemeService.getAllThemeList();
            return Response.success(themeList);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getAllThemeList", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @GetMapping("/banners")
    public Response<List<BannerDto>> getAllBannerList() {
        try {
            List<BannerDto> bannerList = bannerService.getAllBannerList();
            return Response.success(bannerList);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getAllBannerList", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }
}