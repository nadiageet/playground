package com.example.playground.quote.repository;

import com.example.playground.quote.domain.Quote;
import com.example.playground.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query(""" 
            select q.id from Quote q where q not in (select userQuote from User u join u.quoteRegistrations r join r.quote userQuote where u=:user)
            """)
    List<Long> findQuotesNotPossessedByUser(@Param("user") User user);


}
