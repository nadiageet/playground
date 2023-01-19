package com.example.playground.quote.service;

import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.repository.QuoteRegistrationRepository;
import com.example.playground.user.User;
import com.example.playground.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
public class QuoteRegistrationService {
    private final QuoteRegistrationRepository quoteRegistrationRepository;
    private final UserRepository userRepository;

    public QuoteRegistrationService(QuoteRegistrationRepository quoteRegistrationRepository, UserRepository userRepository) {
        this.quoteRegistrationRepository = quoteRegistrationRepository;
        this.userRepository = userRepository;
    }

    public Set<QuoteRegistration> getAllQuoteRegistrations() {
        User user = getAuthenticatedUser();
        return quoteRegistrationRepository.findAllByUser(user);
    }

    private User getAuthenticatedUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("user %s was not found in database".formatted(userName)));
    }

    public Set<QuoteRegistration> getAllProposedQuotes() {
        User user = getAuthenticatedUser();
        return quoteRegistrationRepository.findAllProposedNotUser(user);
    }
}
