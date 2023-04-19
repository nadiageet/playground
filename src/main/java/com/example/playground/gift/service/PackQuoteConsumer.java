package com.example.playground.gift.service;

import com.example.playground.gift.model.Gift;
import com.example.playground.quote.service.QuoteRegistrationService;
import com.example.playground.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("PACK_OF_QUOTE")
@Slf4j
@Transactional
public non-sealed class PackQuoteConsumer implements GiftConsumer {

    private final int packSize;
    private final QuoteRegistrationService quoteRegistrationService;
    private final UserService userService;

    public PackQuoteConsumer(@Value("${app.features.gift.pack.size:5}") int packSize,
                             QuoteRegistrationService quoteRegistrationService, UserService userService) {
        this.packSize = packSize;
        this.quoteRegistrationService = quoteRegistrationService;
        this.userService = userService;
    }

    @Override
    public void consume(Gift gift) {
        log.info("consuming gift {}", gift.getId());
        quoteRegistrationService.openPackForUser(
                userService.getAuthenticatedUser(),
                packSize
        );
    }
}
