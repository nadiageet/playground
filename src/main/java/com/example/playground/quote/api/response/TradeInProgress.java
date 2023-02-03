package com.example.playground.quote.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeInProgress {

    private TradePart initiator;
    private TradePart validator;
}

