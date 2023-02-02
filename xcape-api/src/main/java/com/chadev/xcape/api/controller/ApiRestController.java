package com.chadev.xcape.api.controller;

import com.chadev.xcape.api.controller.response.ThemeWithReservationsResponse;
import com.chadev.xcape.api.service.ReservationService;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
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

    //    admin module 과 중복 ---start
    @GetMapping("/merchants")
    public Response<List<MerchantDto>> getAllMerchants() {
        try {
            List<MerchantDto> merchantDtoList = coreMerchantService.getAllMerchants();
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
//    admin module 과 중복 ---end

    @GetMapping("/merchants/{merchantId}/reservations")
    public Response<List<ThemeWithReservationsResponse>> getThemesWithReservations(
            @PathVariable Long merchantId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
            ) {
        List<ThemeDto> themeDtos = coreThemeService.getThemesByMerchantId(merchantId);
        List<ThemeWithReservationsResponse> response = new ArrayList<>();
        for (ThemeDto themeDto : themeDtos) {
            response.add(new ThemeWithReservationsResponse(
                    themeDto.getId(),
                    themeDto.getNameKo(),
                    themeDto.getNameEn(),
                    themeDto.getMainImagePath(),
                    themeDto.getMinParticipantCount(),
                    themeDto.getMaxParticipantCount(),
                    themeDto.getDifficulty(),
                    reservationService.getReservationsByThemeIdAndDate(themeDto.getId(), date)
            ));
        }

        return Response.success(response);
    }

    @PostMapping("/merchants/{merchantId}/reservations-batch")
    public Response<Void> createBatchReservations(
            @PathVariable Long merchantId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) throws IllegalAccessException {
        reservationService.createEmptyReservationByMerchantId(merchantId, date);
        return Response.success();
    }
}
