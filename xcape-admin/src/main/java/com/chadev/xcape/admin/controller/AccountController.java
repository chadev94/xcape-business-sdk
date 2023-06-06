package com.chadev.xcape.admin.controller;

import com.chadev.xcape.admin.controller.request.AccountRegisterRequest;
import com.chadev.xcape.admin.service.AccountService;
import com.chadev.xcape.core.domain.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/sign-in")
    public String signIn(@RequestBody AccountRegisterRequest request) {
        AccountDto account = accountService.createAccount(request);
        return "redirect:/";
    }
}
