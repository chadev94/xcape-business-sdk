package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.controller.request.MockReservationRequest;
import com.chadev.xcape.admin.controller.request.RangeMockReservationRequest;
import com.chadev.xcape.admin.service.*;
import com.chadev.xcape.core.domain.dto.*;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.request.ReservationRequest;
import com.chadev.xcape.core.domain.request.ThemeModifyRequestDto;
import com.chadev.xcape.core.response.Response;
import com.chadev.xcape.core.service.CoreMerchantService;
import com.chadev.xcape.core.service.CorePriceService;
import com.chadev.xcape.core.service.CoreThemeService;
import com.chadev.xcape.core.service.CoreTimetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminRestController {

    private final CoreMerchantService coreMerchantService;
    private final CoreThemeService coreThemeService;
    private final ThemeService themeService;
    private final CorePriceService corePriceService;
    private final ReservationService reservationService;
    private final BannerService bannerService;
    private final CoreTimetableService coreTimetableService;
    private final MockReservationService mockReservationService;
    private final ReservationHistoryService reservationHistoryService;

    @GetMapping("/merchants")
    public Response<List<MerchantDto>> getAllMerchantsWithThemes() {
        List<MerchantDto> merchantDtoList = coreMerchantService.getAllMerchantsWithThemes();
        return Response.success(merchantDtoList);
    }

    @GetMapping("/merchants/{merchantId}")
    public Response<MerchantDto> getMerchantById(@PathVariable Long merchantId) {
        MerchantDto merchantDto = coreMerchantService.getMerchantWithThemeList(merchantId);
        return Response.success(merchantDto);
    }

    @GetMapping("/themes/{themeId}")
    public Response<ThemeDto> getThemeDetail(@PathVariable Long themeId) {
        ThemeDto theme = coreThemeService.getThemeDetail(themeId);
        return Response.success(theme);
    }

    @GetMapping("/merchants/{merchantId}/themes")
    public Response<List<ThemeDto>> getThemeListByMerchantId(@PathVariable Long merchantId) {
        List<ThemeDto> themeListByMerchantId = coreThemeService.getThemeListByMerchantId(merchantId);
        return Response.success(themeListByMerchantId);
    }

    @PostMapping("/merchants/{merchantId}/themes")
    public Response<Void> createThemeByMerchantId(@PathVariable Long merchantId, ThemeModifyRequestDto requestDto, List<PriceDto> priceDtoList,
                                                  MultipartHttpServletRequest request) throws IOException {
        themeService.createThemeByMerchantId(merchantId, requestDto, request, priceDtoList);
        return Response.success();
    }

    @PutMapping("/themes/{themeId}")
    public Response<Void> modifyThemeById(@PathVariable Long themeId, ThemeModifyRequestDto themeDto,
                                          MultipartHttpServletRequest request) throws IOException {
        themeService.modifyThemeDetail(themeId, themeDto, request);
        return Response.success();
    }

    @GetMapping("/themes/{themeId}/price")
    public Response<List<PriceDto>> getPriceListByThemeId(@PathVariable Long themeId) {
        List<PriceDto> priceListByThemeId = corePriceService.getPriceListByThemeId(themeId);
        return Response.success(priceListByThemeId);
    }

    @GetMapping("/themes/{themeId}/timetable")
    public Response<List<TimetableDto>> getTimetableListByThemeId(@PathVariable Long themeId) {
        List<TimetableDto> priceListByThemeId = coreTimetableService.getTimetableListByThemeId(themeId);
        return Response.success(priceListByThemeId);
    }

    @PutMapping("/themes/{themeId}/timetable")
    public Response<Void> modifyTimetableListByThemeId(@PathVariable Long themeId, @RequestBody List<TimetableDto> timetableDtoList) {
        coreTimetableService.modifyTimetableListByThemeId(timetableDtoList, themeId);
        return Response.success();
    }

    // 지점별 빈 예약 생성
    @PostMapping("/reservation-batch")
    public Response<Void> reservationBatch(LocalDate date) {
        reservationService.reservationBatch(date);

        return Response.success();
    }

    // 예약 등록/수정
    @PutMapping("/reservations/{reservationId}")
    public Response<ReservationDto> registerReservation(@PathVariable String reservationId, @RequestBody ReservationRequest request) {
        ReservationDto savedReservation = reservationService.registerReservationById(reservationId, request);

        return Response.success(savedReservation);
    }

    @GetMapping("/reservations/{reservationId}")
    public Response<ReservationDto> getReservation(@PathVariable String reservationId) {
        ReservationDto reservationDto = reservationService.getReservation(reservationId);

        return Response.success(reservationDto);
    }

    // 예약 취소
    @DeleteMapping("/reservations/{reservationId}")
    public Response<Void> cancelReservation(@PathVariable String reservationId) {
        reservationService.cancelReservationById(reservationId);
        return Response.success();
    }

    @PutMapping("/themes/{themeId}/price")
    public Response<Void> modifyPriceListByThemeId(@PathVariable Long themeId, @RequestBody List<PriceDto> priceDtoList) {
        corePriceService.modifyPriceListByThemeId(priceDtoList, themeId);
        return Response.success();
    }

    @GetMapping("/merchants/{merchantId}/banners")
    public Response<List<BannerDto>> getBannerListByMerchantId(@PathVariable Long merchantId) {
        List<BannerDto> bannerListByMerchantId = bannerService.getBannerListByMerchantId(merchantId);
        return Response.success(bannerListByMerchantId);
    }

    @PostMapping("/merchants/{merchantId}/banners")
    public Response<Void> createBannerByMerchantId(@PathVariable Long merchantId, BannerDto bannerDto,
                                                   MultipartHttpServletRequest request) throws IOException {
        bannerService.createBannerByMerchantId(merchantId, bannerDto, request);
        return Response.success();
    }

    @PutMapping("/merchants/{merchantId}/banners")
    public Response<Void> modifyBannerListByMerchantId(@PathVariable Long merchantId, @RequestBody List<BannerDto> bannerDtoList) {
        bannerService.modifyBannerListByMerchantId(merchantId, bannerDtoList);
        return Response.success();
    }

    @GetMapping("/banners/{bannerId}")
    public Response<BannerDto> getBannerDetail(@PathVariable Long bannerId) {
        BannerDto bannerDetail = bannerService.getBannerDetail(bannerId);
        return Response.success(bannerDetail);
    }

    @PutMapping("/banners/{bannerId}")
    public Response<Void> modifyBannerDetail(@PathVariable Long bannerId, BannerDto bannerDto, MultipartHttpServletRequest request) throws IOException {
        bannerService.modifyBannerDetail(bannerId, bannerDto, request);
        return Response.success();
    }

    // 가예약 등록
    @PutMapping("/mock-reservations")
    public Response<Void> registerMockReservations(@RequestBody MockReservationRequest request) {
        mockReservationService.registerMockReservations(request.getReservationIdList(), request.getUnreservedTime());
        return Response.success();
    }

    // 일괄 가예약 등록
    @PostMapping("/mock-reservations")
    public Response<Void> registerBatchMockReservations(@RequestBody RangeMockReservationRequest rangeMockReservationRequest) {
        mockReservationService.registerRangeMockReservations(rangeMockReservationRequest);
        return Response.success();
    }

    @GetMapping("/reservation-histories")
    public Response<List<ReservationHistoryDto>> getReservationHistoryListByReservationSeq(long reservationSeq) {
        List<ReservationHistoryDto> reservationHistoryList = reservationHistoryService.getReservationHistoryListByReservationSeq(reservationSeq);
        return Response.success(reservationHistoryList);
    }
}
