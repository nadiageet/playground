package com.example.playground.quote;

import com.example.playground.quote.api.response.MarketQuoteRegistration;
import com.example.playground.quote.api.response.QuoteResponse;
import com.example.playground.quote.domain.QuoteRegistration;

import java.util.List;
import java.util.Set;

public class QuoteRegistrationMapper {

    public static List<QuoteResponse> createQuoteRegistration(Set<QuoteRegistration> entities) {
        return entities.stream().
                map(quoteRegistration -> new QuoteResponse(
                        quoteRegistration.getId(),
                        quoteRegistration.getQuote().getId(),
                        quoteRegistration.getQuote().getContent(),
                        quoteRegistration.isProposedQuote()
                ))
                .toList();
    }

    public static List<MarketQuoteRegistration> mapToMarket(Set<QuoteRegistration> entities) {
        return entities.stream()
                .map(quoteRegistration -> new MarketQuoteRegistration(
                        quoteRegistration.getUser().getUserName(),
                        quoteRegistration.getQuote().getOriginator(),
                        quoteRegistration.getQuote().getId(),
                        quoteRegistration.getId()
                )).toList();
    }
}
