package com.example.playground.quote.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetQuotedexResponse(Long id,
                                  String content,
                                  String originator,
                                  boolean isPossessed) {
    

}
