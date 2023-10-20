package com.chadev.xcape.api.controller;

import com.chadev.xcape.api.controller.request.AuthenticationRequest;
import com.chadev.xcape.api.controller.response.ThemeWithReservationsResponse;
import com.chadev.xcape.api.service.*;
import com.chadev.xcape.core.domain.dto.*;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.request.ReservationRequest;
import com.chadev.xcape.core.exception.ApiException;
import com.chadev.xcape.core.exception.ErrorCode;
import com.chadev.xcape.core.response.ReservationHistoryTableDto;
import com.chadev.xcape.core.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
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
    private final ThemeService themeService;
    private final MerchantService merchantService;
    private final AbilityService abilityService;
    private final BannerService bannerService;
    private final StringEncryptor jasyptStringEncryptor;
    private final ReservationHistoryService reservationHistoryService;

    //    admin module 과 중복 ---start
    @GetMapping("/merchants")
    public Response<List<MerchantDto>> getAllMerchantsWithThemes() {
        List<MerchantDto> merchantList = merchantService.getAllMerchantsWithThemes();
        return Response.success(merchantList);
    }

    @GetMapping("/merchants/{merchantId}")
    public Response<MerchantDto> getMerchantById(@PathVariable Long merchantId) {
        MerchantDto merchant = merchantService.getMerchantWithAllInfo(merchantId);
        return Response.success(merchant);
    }

    @GetMapping("/themes/{themeId}")
    public Response<ThemeDto> getThemeById(@PathVariable Long themeId) {
        ThemeDto theme = themeService.getThemeDetail(themeId);
        return Response.success(theme);
    }
//    admin module 과 중복 ---end

    // 예약 페이지용 지점별 예약현황 조회
    @GetMapping(value = "/reservations", params = {"merchantId", "date"})
    public Response<List<ThemeWithReservationsResponse>> getThemesWithReservations(
            Long merchantId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        List<ThemeDto> themeDtoList = themeService.getThemeIdListByMerchantId(merchantId);
        List<ThemeWithReservationsResponse> response = new ArrayList<>();
        themeDtoList.forEach(themeDto -> {
            List<ReservationDto> reservationList = reservationService.getReservationsByThemeIdAndDate(themeDto.getId(), date);
            response.add(ThemeWithReservationsResponse.from(themeDto, reservationList));
        });
        return Response.success(response);
    }

    // 예약 등록
    @PutMapping("/reservations/{reservationId}")
    public Response<ReservationHistoryDto> registerReservation(@PathVariable String reservationId, @RequestBody ReservationRequest request) {
        ReservationHistoryDto reservationHistoryDto = reservationService.registerProcess(reservationId, request);
        return Response.success(reservationHistoryDto);
    }

    // 예약 취소
    @DeleteMapping("/reservations/{reservationHistoryId}")
    public Response<Void> cancelReservation(@PathVariable String reservationHistoryId, @RequestBody ReservationRequest reservationRequest) {
        reservationService.cancelProcess(reservationHistoryId, reservationRequest);
        return Response.success();
    }

    @GetMapping(value = "/reservation-histories")
    public Response<List<ReservationHistoryTableDto>> getReservations(String phoneNumber) {
        List<ReservationHistoryTableDto> reservationHistoryList = reservationHistoryService.getReservationHistoryList(phoneNumber);

        return Response.success(reservationHistoryList);
    }

    @GetMapping("/reservation-histories/{reservationId}")
    public Response<ReservationHistoryDto> getReservationHistoryList(@PathVariable String reservationId) {
        ReservationHistoryDto reservationDto = reservationHistoryService.getReservationHistory(reservationId);

        return Response.success(reservationDto);
    }

    @GetMapping("/themes")
    public Response<List<ThemeDto>> getAllThemeList() {
        List<ThemeDto> themeList = themeService.getAllThemeList();
        return Response.success(themeList);
    }

    @GetMapping("/banners")
    public Response<List<BannerDto>> getAllBannerList() {
        List<BannerDto> bannerList = bannerService.getAllBannerList();
        return Response.success(bannerList);
    }

    @PostMapping("/reservations/authentication")
    public Response<ReservationAuthenticationDto> reservationsAuthentication(@RequestBody AuthenticationRequest authenticationRequest) {
        ReservationAuthenticationDto reservationAuthenticationDto;
        try {
            reservationAuthenticationDto = reservationService.sendAuthenticationMessage(authenticationRequest);
        } catch (ApiException e) {
            log.info(">>> ApiRestController.reservationsAuthentication > ApiException error", e);
            return Response.error(e.getMessage());
        } catch (Exception e) {
            log.info(">>> ApiRestController.reservationsAuthentication > Exception error", e);
            return Response.error(ErrorCode.SERVER_ERROR);
        }

        return Response.success(reservationAuthenticationDto);
    }

    @GetMapping("/abilities")
    public Response<List<AbilityDto>> getAllAbilities() {
        List<AbilityDto> abilityList = abilityService.getAllAbilityList();
        return Response.success(abilityList);
    }

    @GetMapping("/encrypt")
    public Response<String> getEncryptText(HttpServletRequest request, String message) {
        String encryptMessage = jasyptStringEncryptor.encrypt(message);
        return Response.success(encryptMessage);
    }

    @GetMapping("/api-version")
    public String getApiVersion() {
        return "openRoomVersion";
    }

    @GetMapping("hints/{themeId}")
    public ThemeDto getThemeListByThemeId(@PathVariable Long themeId) {
        return themeService.getThemeHintList(themeId);
    }
}