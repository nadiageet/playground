package com.example.playground;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class QuoteRegistration {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;


    @ManyToOne
    @JoinColumn(name = "quote_id")
    Quote quote;

    
}
