package com.example.playground.gift.batch;

import com.example.playground.gift.model.Gift;
import com.example.playground.gift.model.GiftType;
import com.example.playground.user.model.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class GiveRandomQuoteGift implements ItemProcessor<User, Gift> {
    @Override
    public Gift process(User user) throws Exception {
        Gift gift = new Gift();
        gift.setType(GiftType.RANDOM_QUOTE);
        user.addGift(gift);
        return gift;
    }
}
