package com.example.playground;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Quote {
    @Id
    private Long id;

    private String originator;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    

}
