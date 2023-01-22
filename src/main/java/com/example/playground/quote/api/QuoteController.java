package com.example.playground.quote.api;

import com.example.playground.exception.ApplicationException;
import com.example.playground.feign.rapidapi.RandomQuoteClient;
import com.example.playground.quote.QuoteRegistrationMapper;
import com.example.playground.quote.api.request.CreateQuoteRequest;
import com.example.playground.quote.api.request.GenerateQuoteRequest;
import com.example.playground.quote.api.response.GetQuoteRegistrationsResponse;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.repository.QuoteRegistrationRepository;
import com.example.playground.quote.service.QuoteRegistrationService;
import com.example.playground.quote.service.QuoteService;
import com.example.playground.user.UserRepository;
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

    private final QuoteRegistrationService quoteRegistrationService;


    public QuoteController(RandomQuoteClient randomQuoteClient, QuoteService quoteService, QuoteRegistrationRepository quoteRegistrationRepository, UserRepository userRepository, QuoteRegistrationService quoteRegistrationService) {
        this.randomQuoteClient = randomQuoteClient;
        this.quoteService = quoteService;
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

    @PostMapping("/quote/random")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiModelProperty("Generate a random quote from rapid API and save it")
    public void createRandomQuote(@RequestBody @Valid GenerateQuoteRequest generateQuoteRequest) {
        log.debug("request received to generate random quote from rapid API and save it to the database");
        quoteService.generateQuote(generateQuoteRequest.getGenerationNumber());
    }

    @PostMapping("/quote")
    @PreAuthorize("hasRole('ADMIN')")
//    @Valid valider le DTO
    public void createQuote(@RequestBody @Valid CreateQuoteRequest content){
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

    @PostMapping("/trade/propose/{quoteId}")
    @ApiModelProperty("Propose for exchange a quote by the requester")
    public void proposeQuote(@PathVariable Long quoteId) {
        log.debug("request received to propose for exchange a quote by the requester");
        quoteService.proposeQuote(quoteId);
    }

    @PostMapping("/trade/offer/{quoteId}")
    public void offerQuote(@PathVariable("quoteId") Long quoteId, @RequestParam("requesterId") Long requesterId) {
        // TODO: 19/01/2023 NOT IMPLEMENTED
        quoteService.acceptQuote(quoteId);
    }

}
