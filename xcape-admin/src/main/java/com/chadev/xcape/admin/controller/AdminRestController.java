package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.service.MerchantService;
import com.chadev.xcape.admin.service.PriceService;
import com.chadev.xcape.admin.service.ThemeService;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.dto.PriceDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.response.ErrorCode;
import com.chadev.xcape.core.response.Response;
import com.chadev.xcape.core.service.CoreMerchantService;
import com.chadev.xcape.core.service.CoreThemeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

/**
 * GET "/merchants" -> admin 이 갖고 있는 모든 지점 정보 가져오기
 * POST "/merchants/{merchantId}/themes" -> merchant 에 theme 등록하기
 * PUT "/themes/{themeId}" -> theme 수정
 * DELETE "/themes/{themeId}" -> theme 삭제
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminRestController {

    private final CoreMerchantService coreMerchantService;
    private final CoreThemeService coreThemeService;
    private final MerchantService merchantService;
    private final ThemeService themeService;
    private final PriceService priceService;

    @GetMapping("/merchants")
    public Response<List<MerchantDto>> getAllMerchantsWithThemes() {
        try {
            List<MerchantDto> merchantDtoList = merchantService.getAllMerchantsWithThemes();
            return Response.success(merchantDtoList);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getAllMerchants", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @GetMapping("/merchants/{merchantId}")
    public Response<MerchantDto> getMerchantById(@PathVariable Long merchantId) {
        try {
            MerchantDto merchantDto = coreMerchantService.getMerchantById(merchantId);
            return Response.success(merchantDto);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getMerchantById", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @GetMapping("/themes/{themeId}")
    public Response<ThemeDto> getThemeById(@PathVariable Long themeId) {
        try {
            ThemeDto theme = coreThemeService.getThemeById(themeId);
            return Response.success(theme);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getTheme", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @PostMapping("/merchants/{merchantId}/themes")
    public Response<Void> createThemeByMerchantId(@PathVariable Long merchantId, ThemeDto themeDto, List<PriceDto> priceDtoList,
                                                  MultipartHttpServletRequest request) {
        try {
            themeService.createThemeByMerchantId(merchantId, themeDto, request, priceDtoList);
        } catch (IOException ioException) {
            log.error(">>> AdminRestController >>> createThemeByMerchantId {} ", ioException.getMessage());
            return Response.error(ErrorCode.INVALID_PERMISSION);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> createThemeByMerchantId {} merchantId : {}", e, merchantId);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
        return Response.success();
    }

    @PutMapping("/themes/{themeId}")
    public Response<Void> modifyThemeById(@PathVariable Long themeId, ThemeDto themeDto,
                                          MultipartHttpServletRequest request) {
        try {
            themeService.modifyThemeDetail(themeId, themeDto, request);
        } catch (IOException ioException) {
            log.error(">>> AdminRestController >>> modifyThemeById {} ", ioException.getMessage());
            return Response.error(ErrorCode.INVALID_PERMISSION);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> modifyThemeById > ", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
        return Response.success();
    }

    @GetMapping("/price")
    public Response<List<PriceDto>> getPriceListByThemeId(Long themeId) {
        try {
            List<PriceDto> priceListByThemeId = priceService.getPriceListByThemeId(themeId);
            return Response.success(priceListByThemeId);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getPriceListByThemeId > ", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @PostMapping("/price")
    public Response<Void> savePriceList(@RequestBody List<PriceDto> priceList) {
        try {
            // TODO 리퀘스트 객체 생성
            priceService.savePriceList(priceList, priceList.get(0).getThemeId());
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> savePriceList > ", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
        return Response.success();
    }
}
