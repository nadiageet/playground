package com.example.playground;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@Slf4j
public class QuoteRegistrationService {
    private final QuoteRegistrationRepository quoteRegistrationRepository;
    private final UserRepository userRepository;

    public QuoteRegistrationService(QuoteRegistrationRepository quoteRegistrationRepository, UserRepository userRepository) {
        this.quoteRegistrationRepository = quoteRegistrationRepository;
        this.userRepository = userRepository;
    }

    public Set<QuoteRegistration> getAllQuoteRegistrations() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        return quoteRegistrationRepository.findAllByUser(user);
    }

    public Set<QuoteRegistration> getAllProposedQuotes() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        return quoteRegistrationRepository.findAllProposedNotUser(user);
    }
}
