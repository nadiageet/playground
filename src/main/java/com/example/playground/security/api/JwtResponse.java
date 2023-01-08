package com.example.playground.security.api;

import com.example.playground.UserRole;

import java.util.Set;

public record JwtResponse(
        String jwt,
        String userName,
        Set<String> roles
) {
}
