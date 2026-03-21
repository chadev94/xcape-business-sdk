package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.controller.request.CancelReservationRequestDto;
import com.chadev.xcape.admin.controller.request.MockReservationRequest;
import com.chadev.xcape.admin.controller.request.RangeMockReservationRequest;
import com.chadev.xcape.admin.service.*;
import com.chadev.xcape.core.domain.dto.*;
import com.chadev.xcape.core.domain.dto.history.ReservationHistoryDto;
import com.chadev.xcape.core.domain.request.ReservationRequest;
import com.chadev.xcape.core.domain.request.ThemeModifyRequestDto;
import com.chadev.xcape.core.response.Response;
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

    private final MerchantService merchantService;
    private final ThemeService themeService;
    private final PriceService priceService;
    private final ReservationService reservationService;
    private final BannerService bannerService;
    private final TimetableService timetableService;
    private final MockReservationService mockReservationService;
    private final ReservationHistoryService reservationHistoryService;
    private final StorageService storageService;
    private final TagService tagService;
    private final MigrateService migrateService;
    private final HintService hintService;
	private final ViewService viewService;
	private final AbilityService abilityService;

    @GetMapping("/merchants")
    public Response<List<MerchantDto>> getMerchantList() {
        List<MerchantDto> merchantDtoList = merchantService.getMerchantList();
        return Response.success(merchantDtoList);
    }

	@GetMapping("/merchants/{merchantId}")
	public Response<MerchantDto> getMerchantById(@PathVariable Long merchantId) {
		MerchantDto merchantDto = merchantService.getMerchantWithThemeList(merchantId);
		return Response.success(merchantDto);
	}

	@GetMapping("/themes")
	public Response<List<ThemeDto>> getThemeList() {
		List<ThemeDto> themeDtoList = themeService.getThemeList();
		return Response.success(themeDtoList);
	}

	@GetMapping("/themes/{themeId}")
	public Response<ThemeDto> getThemeDetail(@PathVariable Long themeId) {
		ThemeDto theme = themeService.getThemeDetail(themeId);
		return Response.success(theme);
	}

	@GetMapping("/merchants/{merchantId}/themes")
	public Response<List<ThemeDto>> getThemeListByMerchantId(@PathVariable Long merchantId) {
		List<ThemeDto> themeListByMerchantId = themeService.getThemeListByMerchantId(merchantId);
		return Response.success(themeListByMerchantId);
	}

	@PostMapping("/merchants/{merchantId}/themes")
	public Response<ThemeDto> createThemeByMerchantId(
		@PathVariable Long merchantId,
		ThemeModifyRequestDto requestDto,
		MultipartHttpServletRequest request) throws IOException {
		ThemeDto createdTheme = themeService.createThemeByMerchantId(merchantId, requestDto, request);
		return Response.success(createdTheme);
	}

	@PutMapping("/themes/{themeId}")
	public Response<Void> modifyThemeById(@PathVariable Long themeId, ThemeModifyRequestDto themeDto,
		MultipartHttpServletRequest request) throws IOException {
		themeService.modifyThemeDetail(themeId, themeDto, request);
		return Response.success();
	}

	@GetMapping("/themes/{themeId}/price")
	public Response<List<PriceDto>> getPriceListByThemeId(@PathVariable Long themeId) {
		List<PriceDto> priceListByThemeId = priceService.getPriceListByThemeId(themeId);
		return Response.success(priceListByThemeId);
	}

	@GetMapping("/themes/{themeId}/timetable")
	public Response<List<TimetableDto>> getTimetableListByThemeId(@PathVariable Long themeId) {
		List<TimetableDto> priceListByThemeId = timetableService.getTimetableListByThemeId(themeId);
		return Response.success(priceListByThemeId);
	}

	@PutMapping("/themes/{themeId}/timetable")
	public Response<Void> modifyTimetableListByThemeId(@PathVariable Long themeId,
		@RequestBody List<TimetableDto> timetableDtoList) {
		timetableService.modifyTimetableListByThemeId(timetableDtoList, themeId);
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
	public Response<ReservationDetailDto> registerReservation(@PathVariable String reservationId,
															  @RequestBody ReservationRequest request) {
		ReservationDetailDto savedReservation = reservationService.registerReservationById(reservationId, request);

		return Response.success(savedReservation);
	}

	@GetMapping("/reservations/{reservationId}")
	public Response<ReservationDetailDto> getReservation(@PathVariable String reservationId) {
		ReservationDetailDto reservationDetailDto = reservationService.getReservation(reservationId);

		return Response.success(reservationDetailDto);
	}

	// 예약 취소
	@DeleteMapping("/reservations/{reservationId}")
	public Response<Void> cancelReservation(@PathVariable String reservationId) {
		reservationService.cancelReservationById(reservationId);
		return Response.success();
	}

	// 예약 일괄 취소
	@PostMapping("/reservations/cancel-batch")
	public Response<Void> cancelReservations(@RequestBody CancelReservationRequestDto request) {
		reservationService.cancelReservationsByIds(request.getReservationIdList());
		return Response.success();
	}

	@PutMapping("/themes/{themeId}/price")
	public Response<Void> modifyPriceListByThemeId(@PathVariable Long themeId,
		@RequestBody List<PriceDto> priceDtoList) {
		priceService.modifyPriceListByThemeId(priceDtoList, themeId);
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
	public Response<Void> modifyBannerListByMerchantId(@PathVariable Long merchantId,
		@RequestBody List<BannerDto> bannerDtoList) {
		bannerService.modifyBannerListByMerchantId(merchantId, bannerDtoList);
		return Response.success();
	}

	@GetMapping("/banners/{bannerId}")
	public Response<BannerDto> getBannerDetail(@PathVariable Long bannerId) {
		BannerDto bannerDetail = bannerService.getBannerDetail(bannerId);
		return Response.success(bannerDetail);
	}

	@PutMapping("/banners/{bannerId}")
	public Response<Void> modifyBannerDetail(@PathVariable Long bannerId, BannerDto bannerDto,
		MultipartHttpServletRequest request) throws IOException {
		bannerService.modifyBannerDetail(bannerId, bannerDto, request);
		return Response.success();
	}

	// 배너 전체 목록 가져오기
	@GetMapping("/banners")
	public Response<List<BannerDto>> getAllBanners() {
		return Response.success(bannerService.getAllBanners());
	}

	// 가예약 등록
	@PutMapping("/mock-reservations")
	public Response<Void> registerMockReservations(@RequestBody MockReservationRequest request) {
		mockReservationService.registerMockReservations(request.getReservationIdList(), request.getUnreservedTime());
		return Response.success();
	}

	// 일괄 가예약 등록
	@PostMapping("/mock-reservations")
	public Response<Void> registerBatchMockReservations(
		@RequestBody RangeMockReservationRequest rangeMockReservationRequest) {
		mockReservationService.registerRangeMockReservations(rangeMockReservationRequest);
		return Response.success();
	}

	@GetMapping("/reservation-histories")
	public Response<List<ReservationHistoryDto>> getReservationHistoryListByReservationSeq(long reservationSeq) {
		List<ReservationHistoryDto> reservationHistoryList = reservationHistoryService.getReservationHistoryListByReservationSeq(
			reservationSeq);
		return Response.success(reservationHistoryList);
	}

	// 가맹점 등록
	@PostMapping("/merchants")
	public Response<Void> createMerchant(MerchantDto merchantDto) {
		merchantService.createMerchant(merchantDto);
		return Response.success();
	}

	// 가맹점 수정
	@PutMapping("/merchants/{merchantId}")
	public Response<Void> modifyMerchant(@PathVariable Long merchantId, MerchantDto merchantDto) {
		merchantService.modifyMerchant(merchantId, merchantDto);
		return Response.success();
	}

	@GetMapping("/themes/{themeId}/hints")
	public Response<List<HintDto>> getHintListByThemeId(@PathVariable Long themeId) {
		List<HintDto> hintListByThemeId = hintService.getHintListByThemeId(themeId);
		return Response.success(hintListByThemeId);
	}

	// 힌트 생성
	@PostMapping("/hints")
	public Response<Void> createHint(HintDto hintDto) {
		hintService.createHint(hintDto);
		return Response.success();
	}

	// 힌트 수정
	@PutMapping("/hints/{hintId}")
	public Response<Void> modifyHint(@PathVariable Long hintId, HintDto hintDto) {
		hintService.modifyHint(hintId, hintDto);
		return Response.success();
	}

	@GetMapping("/hints")
	public Response<List<HintDto>> getHintList() {
		List<HintDto> hintListByThemeId = hintService.getHintList();
		return Response.success(hintListByThemeId);
	}

	@PostMapping("/files")
	public Response<Void> createFile(FileUploadDto fileUploadDto) {
		storageService.createFile(fileUploadDto);
		return Response.success();
	}

    @GetMapping("/tags")
    public Response<List<TagDto>> getTagList() {
        List<TagDto> tagList = tagService.getTagList();
        return Response.success(tagList);
    }

    @GetMapping("/tag-search")
    public Response<List<TagDto>> getTagListByThemeId(Long themeId) {
        List<TagDto> tagList = tagService.getTagListByThemeId(themeId);
        return Response.success(tagList);
    }

	@GetMapping("/views")
	public Response<List<ViewDto>> getViewList() {
		List<ViewDto> viewList = viewService.getViewList();
		return Response.success(viewList);
	}

	@GetMapping("/storage")
	public Response<List<StorageDto>> getStorageList() {
		List<StorageDto> storageList = storageService.getStorageList();
		return Response.success(storageList);
	}

	@PutMapping("/views")
	public Response<Void> modifyViewList(@RequestBody List<ViewDto> viewList) {
		viewService.modifyViewList(viewList);
		return Response.success();
	}

    @PostMapping("/migrate-storage")
    public void migrateStorageList(@RequestBody List<StorageDto> storageDataList) {
        migrateService.migrateStorageData(storageDataList);
    }

    @PostMapping("/migrate-tag")
    public void migrateTagList(@RequestBody List<TagDto> tagDtoList) {
        migrateService.migrateTagData(tagDtoList);
    }

    @PostMapping("/migrate-view")
    public void migrateViewList(@RequestBody List<ViewDto> viewDtoList) {
        migrateService.migrateViewData(viewDtoList);
    }

	@PostMapping("/tags")
	public Response<Void> createTag(@RequestBody TagDto tagDto) {
		tagService.createTag(tagDto);
		return Response.success();
	}

	@PostMapping("/reservations")
	public Response<Void> createEmptyReservations(Long themeId, LocalDate date) {
		reservationService.createEmptyReservationByThemeIdAndDate(themeId, date);
		return Response.success();
	}

	@GetMapping("/abilities")
	public Response<List<AbilityDto>> getAllAbilities() {
		List<AbilityDto> abilityList = abilityService.getAllAbilityList();
		return Response.success(abilityList);
	}

	@DeleteMapping("/storage/{storageId}")
	public Response<Void> getStorageList(@PathVariable Long storageId) {
		storageService.deleteStorage(storageId);
		return Response.success();
	}

	@DeleteMapping("/tags/{tagId}")
	public Response<Void> deleteTag(@PathVariable Long tagId) {
		tagService.deleteTag(tagId);
		return Response.success();
	}
}
