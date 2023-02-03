package com.example.playground.quote.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter

public class UserResponse {
    
    private Long id;
    private String userName;
    
    private Set<String> role;
}
