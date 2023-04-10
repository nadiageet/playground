package com.example.playground.quote.repository;

import com.example.playground.quote.domain.Quote;
import com.example.playground.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    @Query("from User u join fetch u.roles where u.userName = :userName")
    Optional<User> findByUserNameFetchRoles(@Param("userName") String username);


    @Query("""
                select q from User user join user.quoteRegistrations registrations \
                join registrations.quote q \
                where user=:user
            """)
    Set<Quote> findAllQuotesByUser(@Param("user") User user);
}
