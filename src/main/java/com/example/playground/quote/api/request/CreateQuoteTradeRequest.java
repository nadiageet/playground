package com.example.playground.quote.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuoteTradeRequest {

    private Long quoteRegistrationId;
    private Long quoteInitiatorId;

}
