package com.example.playground.user;

import com.example.playground.quote.domain.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    
    @Query("""
        select q from User user join user.quoteRegistrations registrations \
        join registrations.quote q \
        where user=:user
    """)
    Set<Quote> findAllQuotesByUser(@Param("user") User user);
}
