package com.example.playground.user.api.request;

import com.example.playground.user.model.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterRequest {
    
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    
    private UserRole role = UserRole.COLLECTOR;
}
