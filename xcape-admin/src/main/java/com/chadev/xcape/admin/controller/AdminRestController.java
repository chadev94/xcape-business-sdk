package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.service.MerchantService;
import com.chadev.xcape.admin.service.ThemeService;
import com.chadev.xcape.admin.util.S3Uploader;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.response.ErrorCode;
import com.chadev.xcape.core.response.Response;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final S3Uploader s3Uploader;

    @GetMapping("/merchants")
    public Response<List<MerchantDto>> getAllMerchants() {
        try {
            List<MerchantDto> merchantDtoList = merchantService.getAllMerchants();
            return Response.success(merchantDtoList);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getAllMerchants {}", e.getMessage());
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @GetMapping("/themes/{themeId}")
    public Response<ThemeDto> getTheme(@PathVariable Long themeId) {
        try {
            ThemeDto theme = themeService.getTheme(themeId);
            return Response.success(theme);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> getTheme {}", e.getMessage());
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
    }

    @PostMapping("/merchants/{merchantId}/themes")
    public Response<Void> createThemeByMerchantId(
            @PathVariable Long merchantId,
            @RequestParam("themeDto") String themeJson,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart("bgImage") MultipartFile bgImage
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ThemeDto themeDto = mapper.readValue(themeJson, ThemeDto.class);
        try {
            themeDto.setMainImage(s3Uploader.upload(mainImage, themeDto.getName()));
            themeDto.setBgImage(s3Uploader.upload(bgImage, themeDto.getName()));
            themeService.createThemeByMerchantId(merchantId, themeDto);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> createThemeByMerchantId {} merchantId : {} themeDto : {}", e, merchantId, themeDto);
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
        return Response.success();
    }

    @PutMapping("/themes/{themeId}")
    public Response<Void> modifyThemeById(@PathVariable Long themeId, @RequestBody ThemeDto themeDto) {
        try {
            themeService.modifyThemeDetail(themeId, themeDto);
        } catch (Exception e) {
            log.error(">>> AdminRestController >>> modifyThemeById {} ", e.getMessage());
            return Response.error(ErrorCode.NOT_EXISTENT_DATA);
        }
        return Response.success();
    }
}
