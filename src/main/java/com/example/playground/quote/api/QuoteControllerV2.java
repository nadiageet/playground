package com.example.playground.quote.api;

import com.example.playground.quote.api.request.GiftQuoteRequest;
import com.example.playground.quote.api.response.CollectionOfTrade;
import com.example.playground.quote.api.response.GetCountQuotedexResponse;
import com.example.playground.quote.api.response.TradeInProgress;
import com.example.playground.quote.api.response.UserResponse;
import com.example.playground.quote.service.QuoteRegistrationService;
import com.example.playground.quote.service.QuoteService;
import com.example.playground.user.UserRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: 18/01/2023  split this controller into multiple domains
@RestController
@Slf4j
@ControllerAdvice
@RequestMapping("/api/v2/")
public class QuoteControllerV2 {


    private final QuoteRegistrationService quoteRegistrationService;
    private final UserRepository userRepository;
    private final QuoteService quoteService;



    public QuoteControllerV2(QuoteRegistrationService quoteRegistrationService, UserRepository userRepository, QuoteService quoteService) {
        this.quoteRegistrationService = quoteRegistrationService;
        this.userRepository = userRepository;
        this.quoteService = quoteService;
    }


    @GetMapping("/quotedex")
    public List<GetCountQuotedexResponse> getQuotedex() {
        return quoteRegistrationService.getCountQuotedex();
    }

    @GetMapping("/tradeInProgress")
    public List<TradeInProgress> getTradeHistory(){
        return quoteService.getTradeInProgress();
    }

    @GetMapping("/CollectionOfTradeByUser")
    public Page<CollectionOfTrade> getTradesByUser(Pageable pageable){
        return quoteService.getTradesByUser(pageable);
    }

    @GetMapping("/users")
    public Page<UserResponse> getAllUsers(Pageable pageable){
        return quoteService.findAllUsers(pageable);
    }

    @PostMapping("/offered")
    @ApiModelProperty("offered quote by the requester")
    public void proposeQuote(@RequestBody GiftQuoteRequest giftQuoteRequest) {
         quoteService.giftQuote(giftQuoteRequest.getUserId(), giftQuoteRequest.getQuoteId());
    }


}
