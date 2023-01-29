package com.example.playground.quote.api;

import com.example.playground.feign.rapidapi.RandomQuoteClient;
import com.example.playground.quote.api.request.CreateQuoteTradeRequest;
import com.example.playground.quote.api.request.UpdateQuoteRequest;
import com.example.playground.quote.api.response.TradeHistory;
import com.example.playground.quote.service.QuoteRegistrationService;
import com.example.playground.quote.service.QuoteService;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@ControllerAdvice
@RequestMapping("/api/v1/trade/")
public class TradeController {

    private final RandomQuoteClient randomQuoteClient;
    private final QuoteService quoteService;

    private final QuoteRegistrationService quoteRegistrationService;

    public TradeController(RandomQuoteClient randomQuoteClient, QuoteService quoteService, QuoteRegistrationService quoteRegistrationService) {
        this.randomQuoteClient = randomQuoteClient;
        this.quoteService = quoteService;
        this.quoteRegistrationService = quoteRegistrationService;
    }

    @PostMapping("/propose/{quoteId}")
    @ApiModelProperty("Propose for exchange a quote by the requester")
    public void proposeQuote(@PathVariable Long quoteId) {
        log.debug("request received to propose for exchange a quote by the requester");
        quoteService.proposeQuote(quoteId);
    }

    // TODO: 26/01/2023 API pour lancer un echange
    @PostMapping
    public void offerQuote(@RequestBody CreateQuoteTradeRequest createQuoteTradeRequest ) {

        quoteService.tradeQuote(createQuoteTradeRequest.getQuoteRegistrationId(), createQuoteTradeRequest.getQuoteInitiatorId());
    }

    @PutMapping("/{tradeId}")
    public void updateRrade(@PathVariable Long tradeId, @RequestBody UpdateQuoteRequest updateQuoteRequest) {
        quoteService.updateTrade(tradeId, updateQuoteRequest.getStatus());
    }
    
    @GetMapping
    public List<TradeHistory> getTradeHistory(){
        return quoteService.getTadeHistory();
    }
    
}
