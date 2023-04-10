package com.example.playground.user.api;

import com.example.playground.user.api.response.AccountInfo;
import com.example.playground.user.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {


    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public AccountInfo getAccountInformationFromAuthentication() {
        return accountService.getUserAccountInfoFromAuthentication();
    }
}
