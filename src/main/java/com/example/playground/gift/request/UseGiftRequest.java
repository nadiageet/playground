package com.example.playground.gift.request;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record UseGiftRequest(
        @NotNull UUID giftId
) {
}
