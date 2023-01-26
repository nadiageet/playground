package com.example.playground.quote.domain;

import com.example.playground.user.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class QuoteTrade {


    @Id
    @GeneratedValue
    private Long quoteTradeId;
    @ManyToOne
    private User userInitiator;

    @ManyToOne
    private QuoteRegistration quoteRegistration;

    @Enumerated(EnumType.STRING)
    private TradeStatus status = TradeStatus.WAITING;

}
