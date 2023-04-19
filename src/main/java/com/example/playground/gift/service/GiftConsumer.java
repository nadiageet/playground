package com.example.playground.gift.service;

import com.example.playground.gift.model.Gift;

public sealed interface GiftConsumer
        permits GuaranteeQuoteConsumer, PackQuoteConsumer, RandomQuoteConsumer {

    void consume(Gift gift);
}
