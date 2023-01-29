package com.example.playground.quote.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteTradeResponse {
    
    private Long id;
    private String content;
    private String originator;
    
}
