package com.example.playground.quote;

import com.example.playground.quote.api.response.QuoteResponse;
import com.example.playground.quote.domain.QuoteRegistration;

import java.util.List;
import java.util.Set;

public class QuoteRegistrationMapper {

    public static List<QuoteResponse> createQuoteRegistration(Set<QuoteRegistration> entities) {
        return entities.stream().
                map(quoteRegistration -> new QuoteResponse(
                        quoteRegistration.getQuote().getId(),
                        quoteRegistration.getId(),
                        quoteRegistration.getQuote().getContent(),
                        quoteRegistration.isProposedQuote()
                ))
                .toList();
    }
}
