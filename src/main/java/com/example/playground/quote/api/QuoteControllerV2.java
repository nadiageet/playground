package com.example.playground.quote.api;

import com.example.playground.quote.api.response.GetCountQuotedexResponse;
import com.example.playground.quote.service.QuoteRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO: 18/01/2023  split this controller into multiple domains
@RestController
@Slf4j
@ControllerAdvice
@RequestMapping("/api/v2/")
public class QuoteControllerV2 {


    private final QuoteRegistrationService quoteRegistrationService;


    public QuoteControllerV2(QuoteRegistrationService quoteRegistrationService) {
        this.quoteRegistrationService = quoteRegistrationService;
    }


    @GetMapping("/quotedex")
    public List<GetCountQuotedexResponse> getQuotedex() {
        return quoteRegistrationService.getCountQuotedex();
    }


}
