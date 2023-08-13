package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.service.ReservationService;
import com.chadev.xcape.admin.service.SchedulerService;
import com.chadev.xcape.core.domain.dto.AccountDto;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.type.AccountType;
import com.chadev.xcape.core.service.CoreMerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminViewController {

    private final CoreMerchantService coreMerchantService;
    private final ReservationService reservationService;
    public final SchedulerService schedulerService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/theme-settings")
    public String themeSettings(Model model, Authentication authentication) {
        AccountDto account = (AccountDto) authentication.getPrincipal();
        List<MerchantDto> merchantList = new ArrayList<>();
        if (account.getType() == AccountType.MASTER) {
            merchantList = coreMerchantService.getAllMerchantsWithThemes();
        } else {
            merchantList.add(coreMerchantService.getMerchantWithThemeList(account.getMerchantId()));
        }
        model.addAttribute("merchantList", merchantList);
        return "index";
    }

    @GetMapping(value = "/reservations")
    public String reservation(
            Model model,
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            Long merchantId,
            Authentication authentication
    ) {
        AccountDto account = (AccountDto) authentication.getPrincipal();

        if (account.getType() == AccountType.MASTER) {
            merchantId = merchantId == null ? 1L : merchantId;
        } else {
            merchantId = account.getMerchantId();
        }

        MerchantDto merchant = coreMerchantService.getMerchant(merchantId);
        List<ThemeDto> themesWithReservations = reservationService.getThemesWithReservations(merchantId, date);
        merchant.setThemeList(themesWithReservations);

        model.addAttribute("merchant", merchant);

        if (account.getType() == AccountType.MASTER) {
            List<MerchantDto> merchantList = coreMerchantService.getMerchantIdAndNameList();
            model.addAttribute("merchantList", merchantList);
        }

        return "reservation";
    }

    @GetMapping("/banner")
    public String banner(Model model, Authentication authentication) {
        AccountDto account = (AccountDto) authentication.getPrincipal();
        List<MerchantDto> merchantList = new ArrayList<>();
        if (account.getType() == AccountType.MASTER) {
            merchantList = coreMerchantService.getAllMerchantsWithThemes();
        } else {
            merchantList.add(coreMerchantService.getMerchantWithThemeList(account.getMerchantId()));
        }
        model.addAttribute("merchantList", merchantList);
        return "banner";
    }

    @GetMapping("/mock-reservations")
    public String mockReservations(Model model, Authentication authentication) {
        AccountDto account = (AccountDto) authentication.getPrincipal();
        List<MerchantDto> merchantList = new ArrayList<>();
        if (account.getType() == AccountType.MASTER) {
            merchantList = coreMerchantService.getAllMerchantList();
        } else {
            merchantList.add(coreMerchantService.getMerchant(account.getMerchantId()));
        }
        model.addAttribute("merchantList", merchantList);
        return "mock-reservations";
    }
}
