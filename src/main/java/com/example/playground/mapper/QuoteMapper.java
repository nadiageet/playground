package com.example.playground.mapper;

import com.example.playground.quote.api.response.QuoteTradeResponse;
import com.example.playground.quote.domain.Quote;
import org.mapstruct.Mapper;

import javax.persistence.MapKey;


@Mapper(componentModel = "spring")
public interface QuoteMapper {

    QuoteTradeResponse mapToQuoteTrade(Quote quote);
    
    
}
