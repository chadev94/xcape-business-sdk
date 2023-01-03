package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.service.MerchantService;
import com.chadev.xcape.core.domain.dto.MerchantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminViewController {

    private final MerchantService merchantService;

    @GetMapping("/")
    public String index(Model model) {
        List<MerchantDto> merchants = merchantService.getAllMerchants();
        model.addAttribute("merchants", merchants);
        return "index";
    }
}
