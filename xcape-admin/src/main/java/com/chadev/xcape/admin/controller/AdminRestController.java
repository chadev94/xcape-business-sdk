package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.service.MerchantService;
import com.chadev.xcape.admin.service.ThemeService;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.response.ErrorCode;
import com.chadev.xcape.core.response.Response;
import com.chadev.xcape.core.domain.dto.ThemeDto;
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

    //    private final CoreThemeService coreThemeService;
//    private final CoreMerchantService coreMerchantService;
    private final MerchantService merchantService;
    private final ThemeService themeService;

    @GetMapping("/merchants")
    public Response<List<MerchantDto>> getAllMerchants() {
        try {
            List<MerchantDto> merchantDtoList = merchantService.getAllMerchants();
            return Response.success(merchantDtoList);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getAllMerchants", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @GetMapping("/merchants/{merchantId}")
    public Response<MerchantDto> getMerchantById(@PathVariable Long merchantId) {
        try {
            MerchantDto merchantDto = merchantService.getMerchantById(merchantId);
            return Response.success(merchantDto);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getMerchantById", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @GetMapping("/themes/{themeId}")
    public Response<ThemeDto> getTheme(@PathVariable Long themeId) {
        try {
            ThemeDto theme = themeService.getTheme(themeId);
            return Response.success(theme);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getTheme", e);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @PostMapping("/merchants/{merchantId}/themes")
    public Response<Void> createThemeByMerchantId(@PathVariable Long merchantId, ThemeDto themeDto,
                                                  MultipartHttpServletRequest request) {
        try {
            themeService.createThemeByMerchantId(merchantId, themeDto, request);
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
}
