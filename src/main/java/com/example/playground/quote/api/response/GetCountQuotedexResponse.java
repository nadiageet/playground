package com.example.playground.quote.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetCountQuotedexResponse(Long id,
                                       String content,
                                       String originator,
                                       int numberOfQuote) {
}
