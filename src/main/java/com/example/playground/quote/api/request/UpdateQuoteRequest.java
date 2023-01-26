package com.example.playground.quote.api.request;

import com.example.playground.quote.domain.TradeStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuoteRequest {

    private TradeStatus status = TradeStatus.WAITING;

}
