package com.example.playground.controller;

import com.example.playground.*;
import com.example.playground.exception.ApplicationException;
import com.example.playground.feign.RandomQuoteClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Set;

// TODO: 18/01/2023  split this controller into multiple domains
@RestController
@Slf4j
@ControllerAdvice
@RequestMapping("/api/v1/")
public class QuoteController {

    private final RandomQuoteClient randomQuoteClient;
    private final QuoteService quoteService;

    private final QuoteRegistrationService quoteRegistrationService;


    public QuoteController(RandomQuoteClient randomQuoteClient, QuoteService quoteService, QuoteRegistrationRepository quoteRegistrationRepository, UserRepository userRepository, QuoteRegistrationService quoteRegistrationService) {
        this.randomQuoteClient = randomQuoteClient;
        this.quoteService = quoteService;
        this.quoteRegistrationService = quoteRegistrationService;
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
    public void createQuote(@RequestBody @Valid GenerateQuoteRequest generateQuoteRequest) {
        quoteService.generateQuote(generateQuoteRequest.getGenerationNumber());
    }

    record ErrorRecord(String message, OffsetDateTime dateTime) {

    }

    @ExceptionHandler(ApplicationException.class)
    public ErrorRecord handle(ApplicationException e, Locale locale) {
        log.info("exception catch here with locale {}", locale);
        return new ErrorRecord(e.getMessage(), OffsetDateTime.now(ZoneId.of("Europe/Paris")));
    }

    @GetMapping("/quotes")
    public Set<Quote> getQuotesForAuthenticatedUser() {

        return quoteService.getAllQuotes();
    }

    @GetMapping("/quotesRegistrations")
    public GetQuoteRegistrationsResponse getAllQuoteRegistrations() {
        Set<QuoteRegistration> entities = quoteRegistrationService.getAllQuoteRegistrations();
        return QuoteRegistrationMapper.createQuoteRegistration(entities);

    }

    @GetMapping("/quotesProposed")
    public GetQuoteRegistrationsResponse getAllQuotesRegistrationsProposed() {
        Set<QuoteRegistration> entities = quoteRegistrationService.getAllProposedQuotes();
        return QuoteRegistrationMapper.createQuoteRegistration(entities);
    }

    @PostMapping("/trade/propose/{quoteId}")
    public void proposeQuote(@PathVariable Long quoteId) {
        quoteService.proposeQuote(quoteId);
    }

    @PostMapping("/trade/offer/{quoteId}")
    public void offerQuote(@PathVariable("quoteId") Long quoteId, @RequestParam("requesterId") Long requesterId) {
        quoteService.acceptQuote(quoteId);
    }

}
