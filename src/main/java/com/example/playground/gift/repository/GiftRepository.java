package com.example.playground.gift.repository;

import com.example.playground.gift.model.Gift;
import com.example.playground.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GiftRepository extends JpaRepository<Gift, UUID> {

    Optional<Gift> findByUserAndId(User user, UUID giftId);

    List<Gift> findByUser(User user);
}
