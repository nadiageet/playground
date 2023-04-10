package com.example.playground.user.api.response;

import com.example.playground.user.model.UserRole;

import java.util.Set;

public record AccountInfo(
        String userName,
        Set<UserRole> roles
) {
}
