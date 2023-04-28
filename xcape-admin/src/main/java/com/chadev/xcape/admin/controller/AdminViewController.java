package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.service.ReservationService;
import com.chadev.xcape.admin.service.SchedulerService;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.service.CoreMerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AdminViewController {

    private final CoreMerchantService coreMerchantService;
    private final ReservationService reservationService;
    public final SchedulerService schedulerService;

    @GetMapping("/")
    public String index(Model model) {
        List<MerchantDto> merchants = coreMerchantService.getAllMerchantsWithThemes();
        model.addAttribute("merchants", merchants);
        return "index";
    }

    @GetMapping(value = "/reservations", params = {"date", "merchantId"})
    public String reservation(
            Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            Long merchantId
    ) {
        List<ThemeDto> themesWithReservations = reservationService.getThemesWithReservations(merchantId, date);
        List<MerchantDto> merchantList = coreMerchantService.getMerchantIdAndNameList();
        model.addAttribute("merchantList", merchantList);
        model.addAttribute("themes", themesWithReservations);
        model.addAttribute("date", date);
        model.addAttribute("merchantId", merchantId);
        return "reservation";
    }

    @GetMapping("/banner")
    public String banner(Model model) {
        List<MerchantDto> merchantList = coreMerchantService.getAllMerchantsWithThemes();
        model.addAttribute("merchantList", merchantList);
        return "banner";
    }
}
