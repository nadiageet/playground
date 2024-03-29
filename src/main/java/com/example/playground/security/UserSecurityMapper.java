package com.example.playground.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserSecurityMapper {


    public static UserDetails map(com.example.playground.user.model.User user) {

        return User.withUsername(user.getUserName())
                .password(user.getPassword())
                .roles(
                        user.getRoles().stream()
                                .map(Enum::toString)
                                .toArray(String[]::new)
                )
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}
