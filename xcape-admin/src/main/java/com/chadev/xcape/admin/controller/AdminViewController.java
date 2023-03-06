package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.service.MerchantService;
import com.chadev.xcape.admin.service.ReservationService;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AdminViewController {

    private final MerchantService merchantService;
    private final ReservationService reservationService;

    @GetMapping("/")
    public String index(Model model) {
        List<MerchantDto> merchants = merchantService.getAllMerchants();
        model.addAttribute("merchants", merchants);
        return "index";
    }

    @GetMapping("/merchants/{merchantId}/reservations")
    public String reservation(
            Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @PathVariable Long merchantId
    ) {
        List<ThemeDto> themesWithReservations = reservationService.getThemesWithReservations(merchantId, date);
        int maxLength = 0;
        for (ThemeDto theme : themesWithReservations)
            maxLength = Math.max(theme.getReservationDtos().size(), maxLength);
        model.addAttribute("themes", themesWithReservations);
        model.addAttribute("maxLength", maxLength);
        model.addAttribute("date", date);
        return "reservation";
    }
}
