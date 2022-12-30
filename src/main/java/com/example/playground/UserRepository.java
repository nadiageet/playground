package com.example.playground;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);
    
    @Query("""
        select q from User user join user.quoteRegistrations registrations \
        join registrations.quote q \
        where user=:user
    """)
    Set<Quote> findAllQuotesByUser(@Param("user") User user);
}
