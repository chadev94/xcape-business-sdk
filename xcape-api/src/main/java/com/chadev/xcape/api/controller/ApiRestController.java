package com.chadev.xcape.api.controller;

import com.chadev.xcape.api.controller.request.AuthenticationRequest;
import com.chadev.xcape.api.controller.request.ReservationCancelRequest;
import com.chadev.xcape.api.controller.request.ReservationRegisterRequest;
import com.chadev.xcape.api.controller.response.ThemeWithReservationsResponse;
import com.chadev.xcape.api.service.BannerService;
import com.chadev.xcape.api.service.ReservationService;
import com.chadev.xcape.core.domain.dto.*;
import com.chadev.xcape.core.exception.ApiException;
import com.chadev.xcape.core.exception.ErrorCode;
import com.chadev.xcape.core.response.ReservationHistoryTableDto;
import com.chadev.xcape.core.response.Response;
import com.chadev.xcape.core.service.CoreAbilityService;
import com.chadev.xcape.core.service.CoreMerchantService;
import com.chadev.xcape.core.service.CoreThemeService;
import com.chadev.xcape.core.service.notification.kakao.KakaoTalkNotification;
import com.chadev.xcape.core.service.notification.sms.SmsNotification;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApiRestController {

    private final ReservationService reservationService;
    private final CoreThemeService coreThemeService;
    private final CoreMerchantService coreMerchantService;
    private final CoreAbilityService coreAbilityService;
    private final BannerService bannerService;
    private final SmsNotification smsSender;
    private final KakaoTalkNotification kakaoSender;
    private final StringEncryptor jasyptStringEncryptor;

    //    admin module 과 중복 ---start
    @GetMapping("/merchants")
    public Response<List<MerchantDto>> getAllMerchantsWithThemes() {
        List<MerchantDto> merchantList = coreMerchantService.getAllMerchantsWithThemes();
        return Response.success(merchantList);
    }

    @GetMapping("/merchants/{merchantId}")
    public Response<MerchantDto> getMerchantById(@PathVariable Long merchantId) {
        MerchantDto merchant = coreMerchantService.getMerchantWithAllInfo(merchantId);
        return Response.success(merchant);
    }

    @GetMapping("/themes/{themeId}")
    public Response<ThemeDto> getThemeById(@PathVariable Long themeId) {
        ThemeDto theme = coreThemeService.getThemeDetail(themeId);
        return Response.success(theme);
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
    public Response<ReservationDto> registerReservation(@PathVariable Long reservationId, @RequestBody ReservationRegisterRequest request) {
        ReservationDto savedReservation = reservationService.registerReservationById(reservationId, request.getReservedBy(), request.getPhoneNumber(), request.getParticipantCount(), request.getRoomType(), request.getRequestId(), request.getAuthenticationNumber());

        return Response.success(savedReservation);
    }

    @GetMapping("/reservations/{reservationId}")
    public Response<ReservationDto> getReservation(@PathVariable String reservationId) {
        ReservationDto reservationDto = reservationService.getReservation(reservationId);

        return Response.success(reservationDto);
    }

    // 예약 취소
    @DeleteMapping("/reservations/{reservationId}")
    public Response<Void> cancelReservation(@PathVariable String reservationId, @RequestBody ReservationCancelRequest request) {
        ReservationDto reservation = reservationService.getReservation(reservationId);
        if (!Objects.equals(reservation.getPhoneNumber(), request.getRecipientNo())) {
            reservationService.cancelReservationById(reservationId, request.getRequestId(), request.getAuthenticationNumber());
            return Response.success();
        } else {    // 예약 연락처와 인증 연락처 미일치
            throw new ApiException(Integer.parseInt(ErrorCode.AUTHENTICATION_INVALID_PHONE_NUMBER.getCode()), ErrorCode.AUTHENTICATION_INVALID_PHONE_NUMBER.getMessage());
        }
    }

    // 연락처로 예약 이력 조회
    @GetMapping(value = "/reservations", params = {"phoneNumber"})
    public Response<List<ReservationHistoryTableDto>> getReservations(String phoneNumber) {
        List<ReservationHistoryTableDto> reservationHistoryTableDtoList = reservationService.getReservationHistoryList(phoneNumber);

        return Response.success(reservationHistoryTableDtoList);
    }

    @GetMapping("/themes")
    public Response<List<ThemeDto>> getAllThemeList() {
        List<ThemeDto> themeList = coreThemeService.getAllThemeList();
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
        List<AbilityDto> abilityList = coreAbilityService.getAllAbilityList();
        return Response.success(abilityList);
    }

    @GetMapping("/encrypt")
    public Response<String> getEncryptText(HttpServletRequest request, String message) {
        String encryptMessage = jasyptStringEncryptor.encrypt(message);
        return Response.success(encryptMessage);
    }
}