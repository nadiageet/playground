package com.example.playground.gift.service;

import com.example.playground.gift.events.GiftRandomQuoteEvent;
import com.example.playground.gift.events.GuaranteeRandomQuoteEvent;
import com.example.playground.gift.model.Gift;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GiftDispatcher {

    private final ApplicationEventPublisher publisher;


    public void dispatch(Gift gift) {
        log.debug("dispatching gift {} of type {}", gift.getId(), gift.getType());
        switch (gift.getType()) {
            case RANDOM_QUOTE -> publisher.publishEvent(new GiftRandomQuoteEvent());
            case GUARANTEE_RANDOM_QUOTE -> {
                publisher.publishEvent(new GuaranteeRandomQuoteEvent());
            }
        }
    }
}
