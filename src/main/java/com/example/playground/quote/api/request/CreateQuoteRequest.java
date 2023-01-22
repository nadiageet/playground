package com.example.playground.quote.api.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateQuoteRequest {
    
    @NotBlank
    String content; 
}
