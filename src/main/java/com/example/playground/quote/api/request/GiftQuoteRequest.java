package com.example.playground.quote.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GiftQuoteRequest {
    
    private Long userId;
    private Long quoteId;
    
}
