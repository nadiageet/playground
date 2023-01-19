package com.example.playground.user.api.response;

import java.util.Set;

public record JwtResponse(
        String jwt,
        String userName,
        Set<String> roles
) {
}
