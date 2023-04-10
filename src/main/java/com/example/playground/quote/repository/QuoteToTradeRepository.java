package com.example.playground.quote.repository;

import com.example.playground.quote.domain.QuoteTrade;
import com.example.playground.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuoteToTradeRepository extends JpaRepository<QuoteTrade, Long> {
    
    @Query("""
            select trade from QuoteTrade trade
            where trade.status = 'ACCEPTED'
            and (trade.userInitiator = :authenticatedUser or trade.userValidator = :authenticatedUser)
            """)
    List<QuoteTrade> getAllTradeAcceptedByUser(User authenticatedUser);

    @Query("""
            select trade from QuoteTrade trade
            where trade.status = 'WAITING' 
            """)
    List<QuoteTrade> getAllTradeInProposedStatus();

    @Query("""
            select trade from QuoteTrade trade
            where trade.userInitiator = :authenticatedUser or trade.userValidator = :authenticatedUser
            """)
    Page<QuoteTrade> getTradesByUser(User authenticatedUser, Pageable pageable);

}
