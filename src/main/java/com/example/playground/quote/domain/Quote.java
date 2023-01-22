package com.example.playground.quote.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Quote {
    
    @Id
    @GeneratedValue
    private Long id;

    private String originator;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    

}
