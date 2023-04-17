package com.example.playground.quote.api;

import com.example.playground.feign.rapidapi.RandomQuoteClient;
import com.example.playground.mapper.QuoteMapper;
import com.example.playground.quote.QuoteRegistrationMapper;
import com.example.playground.quote.api.request.CreateQuoteRequest;
import com.example.playground.quote.api.request.GenerateQuoteRequest;
import com.example.playground.quote.api.request.GiftQuoteRequest;
import com.example.playground.quote.api.response.GetQuoteRegistrationsResponse;
import com.example.playground.quote.api.response.GetQuotedexResponse;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.service.QuoteRegistrationService;
import com.example.playground.quote.service.QuoteService;
import feign.FeignException;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

// TODO: 18/01/2023  split this controller into multiple domains
@RestController
@Slf4j
@ControllerAdvice
@RequestMapping("/api/v1/")
public class QuoteController {

    private final RandomQuoteClient randomQuoteClient;
    private final QuoteService quoteService;

    private final QuoteMapper quoteMapper;

    private final QuoteRegistrationService quoteRegistrationService;


    public QuoteController(RandomQuoteClient randomQuoteClient, QuoteService quoteService,
                           QuoteMapper quoteMapper, QuoteRegistrationService quoteRegistrationService) {
        this.randomQuoteClient = randomQuoteClient;
        this.quoteService = quoteService;
        this.quoteMapper = quoteMapper;
        this.quoteRegistrationService = quoteRegistrationService;
    }


    @GetMapping("/quote/random")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiModelProperty("Get a random quote content from rapid API")
    public String getRandomQuoteAPI() {
        log.debug("request received to get a random quote content from rapid API");
        try {
            return randomQuoteClient.getRandomQuote().content;
        } catch (FeignException e) {
            log.error("error contacting random quote client", e);
            return "";
        }
    }

    @GetMapping("/quote/receive")
    @ApiModelProperty("receive a free quote")
    public GetQuotedexResponse receiveQuote() {
        log.debug("request received to receive a random quote");
        return quoteRegistrationService.receiveQuote();
    }

    @PostMapping("/quote/random")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiModelProperty("Generate a random quote from rapid API and save it")
    public void createRandomQuote(@RequestBody @Valid GenerateQuoteRequest generateQuoteRequest) {
        log.debug("request received to generate random quote from rapid API and save it to the database");
        quoteService.generateQuote(generateQuoteRequest.getGenerationNumber());
    }

    @PostMapping("/quote")
//    @PreAuthorize("hasRole('ADMIN')")
    public void createQuote(@RequestBody @Valid CreateQuoteRequest content) {
        quoteService.createQuote(content.getContent());
    }


    @GetMapping("/quote")
    @ApiModelProperty("Get all the quotes from database owned by the requester")
    public Set<Quote> getQuotesForAuthenticatedUser() {
        log.debug("request received to get all the quotes owned by the requester");
        return quoteService.getAllQuotes();
    }

    @GetMapping("/quotesRegistrations")
    @ApiModelProperty("Get all the quotes registrations owned by the requester")
    public GetQuoteRegistrationsResponse getAllQuoteRegistrations() {
        log.debug("request received to all the quote registrations owned by the requester");
        Set<QuoteRegistration> entities = quoteRegistrationService.getAllQuoteRegistrations();
        return QuoteRegistrationMapper.createQuoteRegistration(entities);

    }

    @GetMapping("/quotesProposed")
    @ApiModelProperty("Get all the quote proposed for exchange by other person than the requester")
    public GetQuoteRegistrationsResponse getAllQuotesRegistrationsProposed() {
        log.debug("request received to all the quote proposed for exchange by other person than the requester");
        Set<QuoteRegistration> entities = quoteRegistrationService.getAllProposedQuotes();
        return QuoteRegistrationMapper.createQuoteRegistration(entities);
    }

    @PostMapping("/gift")
    @ApiModelProperty("proposed quote by the requester")
    public void proposeQuote(@RequestBody GiftQuoteRequest giftQuoteRequest) {
        quoteService.giftQuote(giftQuoteRequest.getUserId(), giftQuoteRequest.getQuoteId());
    }


}
