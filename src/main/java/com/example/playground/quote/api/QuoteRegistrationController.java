package com.example.playground.quote.api;

import com.example.playground.quote.api.response.GetQuotedexResponse;
import com.example.playground.quote.service.QuoteRegistrationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quoteRegistration/")
public class QuoteRegistrationController {
    
    private final QuoteRegistrationService quoteRegistrationService;

    public QuoteRegistrationController(QuoteRegistrationService quoteRegistrationService) {
        this.quoteRegistrationService = quoteRegistrationService;
    }

    @PostMapping("/random")
    public void generateQuote(){
        quoteRegistrationService.giveRandomQuote();
    }
    
    @GetMapping("/quotedex")
    public List<GetQuotedexResponse> getQuotedex(){
        return quoteRegistrationService.getQuotedex();
    }
}
