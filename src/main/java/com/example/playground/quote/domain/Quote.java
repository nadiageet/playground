package com.example.playground.quote.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quote_seq")
    @SequenceGenerator(initialValue = 1, name = "quote_seq", allocationSize = 1)
    private Long id;

    private String originator;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    

}
