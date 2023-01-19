package com.example.playground.quote;

import com.example.playground.quote.api.response.GetQuoteRegistrationsResponse;
import com.example.playground.quote.api.response.QuoteResponse;
import com.example.playground.quote.domain.QuoteRegistration;

import java.util.List;
import java.util.Set;

public class QuoteRegistrationMapper {

    public static GetQuoteRegistrationsResponse createQuoteRegistration(Set<QuoteRegistration> entities) {
        List<QuoteResponse> quoteResponses = entities.stream().
                map(quoteRegistration -> new QuoteResponse(
                        quoteRegistration.getId(),
                        quoteRegistration.getQuote().getContent(),
                        quoteRegistration.isProposedQuote()
                ))
                .toList();
        return new GetQuoteRegistrationsResponse(quoteResponses);
    }
}
