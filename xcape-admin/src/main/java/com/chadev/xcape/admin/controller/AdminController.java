package com.chadev.xcape.admin.controller;

import controller.request.ThemeCreateRequest;
import controller.request.ThemeModifyRequest;
import controller.response.ErrorCode;
import controller.response.Response;
import domain.dto.ThemeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import service.ThemeService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final ThemeService themeService;

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
