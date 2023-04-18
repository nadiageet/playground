package com.example.playground.gift.response;

import com.example.playground.gift.model.GiftType;

import java.time.Instant;
import java.util.UUID;

public record GiftDTO(
        UUID id,
        GiftType type,
        Instant createdAt,

        boolean isUsed,

        Instant usedAt
) {
}
