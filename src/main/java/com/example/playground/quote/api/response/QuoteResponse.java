package com.example.playground.quote.api.response;

public record QuoteResponse(Long registrationId,
                            Long quoteId,
                            String quoteContent,
                            boolean isProposed) {

}
