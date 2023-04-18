package com.example.playground.gift.batch;

import com.example.playground.gift.model.Gift;
import com.example.playground.gift.model.GiftType;
import com.example.playground.user.model.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GiveRandomQuoteGift implements ItemProcessor<User, Gift> {
    private static GiftType getRandomType() {
        double v = new SecureRandom().nextDouble();
        if (v < .1) {
            return GiftType.GUARANTEE_RANDOM_QUOTE;
        } else {
            return GiftType.RANDOM_QUOTE;
        }
    }

    @Override
    public Gift process(User user) throws Exception {
        Gift gift = new Gift();
        gift.setType(getRandomType());
        user.addGift(gift);
        return gift;
    }
}
