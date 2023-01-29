package com.example.playground.quote.api.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class TradeHistory {

    private QuoteTradeResponse quoteReceived;

    private QuoteTradeResponse quoteGiven;

    private String traderName; 
}
