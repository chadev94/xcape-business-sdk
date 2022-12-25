package com.chadev.xcape.admin.controller;

import controller.request.ThemeCreateRequest;
import controller.request.ThemeModifyRequest;
import controller.response.ErrorCode;
import controller.response.MerchantResponse;
import controller.response.Response;
import domain.dto.ThemeDto;
import domain.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import service.MerchantService;
import service.ThemeService;

import java.util.List;

/**
 * GET "/merchants" -> admin 이 갖고 있는 모든 지점 정보 가져오기
 * POST "/merchants/{merchantId}/themes" -> merchant 에 theme 등록하기
 * PUT "/themes/{themeId}" -> theme 수정
 * DELETE "/themes/{themeId}" -> theme 삭제
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final ThemeService themeService;
    private final MerchantService merchantService;

    @GetMapping("/merchants")
    public Response<List<MerchantResponse>> getMerchantsByAdmin(Account account) {
        try {
            List<MerchantResponse> merchantResponseList = merchantService.getMerchantsByAccount(account).stream().map(MerchantResponse::fromDto).toList();
            return Response.success(merchantResponseList);
        } catch (Exception e) {
            return Response.error(ErrorCode.INVALID_PERMISSION);
        }
    }

    @PostMapping("/merchants/{merchantId}/themes")
    public Response<Void> createThemeByMerchantId(@PathVariable Long merchantId, @RequestBody ThemeCreateRequest request) {
        try {
            ThemeDto dto = ThemeDto.fromCreateRequest(request);
            themeService.createThemeByMerchantId(merchantId, dto);
            return Response.success();
        } catch (Exception e) {
            log.error(">>>> error : {} \" {} \" {}", e, e.getCause(), e.getMessage());
            return Response.error(ErrorCode.INVALID_PERMISSION);
        }
    }

    @PutMapping("/themes/{themeId}")
    public Response<Void> modifyThemeById(@PathVariable Long themeId, @RequestBody ThemeModifyRequest request) {
        ThemeDto dto = ThemeDto.fromModifyRequest(request);
        themeService.modifyThemeById(themeId, dto);
        return Response.success();
    }

    @DeleteMapping("/themes/{themeId}")
    public Response<Void> deleteThemeById(@PathVariable Long themeId) {
        themeService.deleteThemeById(themeId);
        return Response.success();
    }
}
