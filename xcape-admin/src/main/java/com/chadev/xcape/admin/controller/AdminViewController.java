package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.controller.response.ReservationResponse;
import com.chadev.xcape.admin.service.MerchantService;
import com.chadev.xcape.admin.service.ReservationService;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminViewController {

    private final MerchantService merchantService;
    private final ReservationService reservationService;

    @GetMapping("/")
    public String index(Model model) {
        List<MerchantDto> merchants = merchantService.getAllMerchantsWithThemes();
        model.addAttribute("merchants", merchants);
        return "index";
    }

    @GetMapping("/{merchantId}/reservations")
    public String reservation(
            Model model,
            @PathVariable Long merchantId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        List<ReservationResponse> reservations = reservationService.findReservedReservation(merchantId, date);

        List<MerchantDto> merchants = merchantService.getAllMerchantsWithThemes();
        model.addAttribute("merchants", merchants);
        model.addAttribute("reservations", reservations);
        return "reservation";
    }
}
