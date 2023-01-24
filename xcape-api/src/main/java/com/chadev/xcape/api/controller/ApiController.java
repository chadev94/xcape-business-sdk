package com.chadev.xcape.api.controller;

import com.chadev.xcape.api.service.ReservationService;
import com.chadev.xcape.core.domain.dto.ReservationDto;
import com.chadev.xcape.core.exception.XcapeException;
import com.chadev.xcape.core.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApiController {

    private final ReservationService reservationService;

    @GetMapping("/{themeId}/{date}")
    public Response<List<ReservationDto>> getReservationsByThemeIdAndDate(@PathVariable String date, @PathVariable Long themeId) {

        //TODO: date 형식 정의 필요
        return Response.success(
                reservationService.getReservationsByThemeAndDate(themeId, LocalDate.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 2)), Integer.parseInt(date.substring(6, 2))))
        );
    }

    @GetMapping("/{merchantId}/{date}")
    public Response<List<ReservationDto>> getReservationsByMerchantIdAndDate(@PathVariable String date, @PathVariable Long merchantId) {
        try {
            //TODO: date 형식 정의 필요
            return Response.success(
                    reservationService.getReservationsByMerchantAndDate(merchantId, LocalDate.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 2)), Integer.parseInt(date.substring(6, 2))))
            );
        } catch (XcapeException.NotExistentMerchantException e) {
            return Response.error(e.errorCode);
        }
    }
}
