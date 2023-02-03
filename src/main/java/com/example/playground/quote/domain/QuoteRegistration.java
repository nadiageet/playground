package com.example.playground.quote.domain;

import com.example.playground.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class QuoteRegistration {
    
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "quote_id")
    private Quote quote;

    private boolean isProposedQuote = false; 
    
    private boolean isOfferedQuote = false;
    
    
    
}
