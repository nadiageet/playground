package com.example.playground.quote.domain;

import com.example.playground.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Data
public class QuoteTrade {


    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User userInitiator;

    @ManyToOne
    private User userValidator;

    // TODO: 27/01/2023 Add a quote proposed by initiator 

    // TODO: 27/01/2023 Quote a la place 
    // TODO: 27/01/2023 Ajouter l'utilisateur avec qui on echange qui detient la quote
    @ManyToOne
    private Quote quoteValidator;
    
    @ManyToOne
    private Quote quoteInitiator;

    @Enumerated(EnumType.STRING)
    private TradeStatus status = TradeStatus.WAITING;

    public Optional<Quote> getQuoteInitiator() {
        return Optional.ofNullable(quoteInitiator);
    }
}
