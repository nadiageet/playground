package com.example.playground.gift;

import com.example.playground.gift.events.GiftRandomQuoteEvent;
import com.example.playground.quote.service.QuoteRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventGiftListener {


    private final QuoteRegistrationService quoteRegistrationService;

    public EventGiftListener(QuoteRegistrationService quoteRegistrationService) {
        this.quoteRegistrationService = quoteRegistrationService;
    }

    @EventListener(GiftRandomQuoteEvent.class)
    public void handle(GiftRandomQuoteEvent event) {
        log.info("handle event GIFT RANDOM QUOTE");
        quoteRegistrationService.receiveQuote();
    }
}

