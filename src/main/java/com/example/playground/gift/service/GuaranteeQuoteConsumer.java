package com.example.playground.gift.service;

import com.example.playground.gift.model.Gift;
import com.example.playground.quote.service.QuoteRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("GUARANTEE_RANDOM_QUOTE")
@Slf4j
public final class GuaranteeQuoteConsumer implements GiftConsumer {
    private final QuoteRegistrationService quoteRegistrationService;

    public GuaranteeQuoteConsumer(QuoteRegistrationService quoteRegistrationService) {
        this.quoteRegistrationService = quoteRegistrationService;
    }

    @Override
    public void consume(Gift gift) {
        log.info("consuming gift {}", gift.getId());
        quoteRegistrationService.receiveQuoteGuarantee();
    }
}
