package com.example.playground.quote.repository;

import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface QuoteRegistrationRepository extends JpaRepository<QuoteRegistration, Long> {
    @Query("""
                select registrations from User user join user.quoteRegistrations registrations \
                 join fetch registrations.quote q where user=:user
            """)
    Set<QuoteRegistration> findAllByUser(User user);

    @Query("""
                select registrations from User u join u.quoteRegistrations registrations \
                 join fetch registrations.quote q where registrations.isProposedQuote is true and u <> :user
            """)
    Set<QuoteRegistration> findAllProposedNotUser(User user);

    // TODO: 27/01/2023 find by user and quote 
}
