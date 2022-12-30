package com.example.playground.controller;

import com.example.playground.GenerateQuoteRequest;
import com.example.playground.Quote;
import com.example.playground.QuoteRepository;
import com.example.playground.QuoteService;
import com.example.playground.feign.RandomQuoteClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
@Slf4j
public class QuoteController {

    private final RandomQuoteClient randomQuoteClient;
    private final QuoteService quoteService;


    public QuoteController(RandomQuoteClient randomQuoteClient, QuoteRepository quoteRepository, QuoteService quoteService) {
        this.randomQuoteClient = randomQuoteClient;
        this.quoteService = quoteService;
    }


    @GetMapping("/quote")
    @PreAuthorize("hasRole('ADMIN')")
    public String getRandomQuoteAPI(Principal principal) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            return randomQuoteClient.getRandomQuote().content;
        } catch (FeignException e) {
            log.error("error contacting random quote client", e);
            return "";
        }
    }

    @PostMapping("/quotes")
    @PreAuthorize("hasRole('ADMIN')")
    public void createQuote(@RequestBody GenerateQuoteRequest generateQuoteRequest) {

        quoteService.generateQuote(generateQuoteRequest.getGenerationNumber());

    }

    @GetMapping("/quotes")
    public Set<Quote> getQuotesForAuthenticatedUser() {

        return quoteService.getAllQuotes();
    }
}
