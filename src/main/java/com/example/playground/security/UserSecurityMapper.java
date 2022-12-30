package com.example.playground.security;

import com.example.playground.UserRole;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;

public class UserSecurityMapper {


    public static UserDetails map(com.example.playground.User user) {

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
