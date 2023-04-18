package com.example.playground.gift.service;

import com.example.playground.exception.ApplicationException;
import com.example.playground.gift.model.Gift;
import com.example.playground.gift.repository.GiftRepository;
import com.example.playground.user.model.User;
import com.example.playground.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GiftService {

    private final UserService userService;

    private final GiftRepository giftRepository;

    private final Clock clock;


    private final GiftDispatcher giftDispatcher;

    public void useGift(UUID giftId) {
        User authenticatedUser = userService.getAuthenticatedUser();

        Gift gift = giftRepository.findByUserAndId(authenticatedUser, giftId)
                .orElseThrow(() -> new ApplicationException("gift.not_found"));

        if (gift.isUsed()) {
            throw new ApplicationException("gift.already_used");
        }

        gift.use(Instant.now(clock));
        log.info("gift [{}] with id \"{}\" used", gift.getType(), giftId);

        giftDispatcher.dispatch(gift);

    }

    public List<Gift> findAllUserGifts() {
        return giftRepository.findByUser(userService.getAuthenticatedUser());
    }

}
