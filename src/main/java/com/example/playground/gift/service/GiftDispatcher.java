package com.example.playground.gift.service;

import com.example.playground.gift.model.Gift;
import com.example.playground.gift.model.GiftType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@Transactional
public class GiftDispatcher {

    private final Map<GiftType, GiftConsumer> delegates;

    public GiftDispatcher(Map<String, GiftConsumer> map) {
        delegates = map.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> GiftType.valueOf(entry.getKey()),
                        Map.Entry::getValue)
                );
    }

    public static void main(String[] args) {
        System.out.println(GiftType.valueOf("RANDOM_QUOTE"));
    }

    public void dispatch(Gift gift) {
        log.debug("dispatching gift {} of type {}", gift.getId(), gift.getType());
        delegates.get(gift.getType()).consume(gift);
    }
}
