package com.example.playground.quote.service;

import com.example.playground.quote.api.response.GetQuotedexResponse;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.repository.QuoteRegistrationRepository;
import com.example.playground.quote.repository.QuoteRepository;
import com.example.playground.user.User;
import com.example.playground.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class QuoteRegistrationService {
    private final QuoteRegistrationRepository quoteRegistrationRepository;
    private final UserRepository userRepository;
    
    private final QuoteRepository quoteRepository;

    public QuoteRegistrationService(QuoteRegistrationRepository quoteRegistrationRepository, UserRepository userRepository, QuoteRepository quoteRepository) {
        this.quoteRegistrationRepository = quoteRegistrationRepository;
        this.userRepository = userRepository;
        this.quoteRepository = quoteRepository;
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

    @Transactional
    public void giveRandomQuote() {
        QuoteRegistration quoteRegistration = new QuoteRegistration();
        User authenticatedUser = getAuthenticatedUser();
        quoteRegistration.setQuote(findRandomQuote());
        authenticatedUser.addRegistration(quoteRegistration);
        
        quoteRegistrationRepository.save(quoteRegistration);
        log.info("user {} has a new quote : {}", authenticatedUser.getUserName(), quoteRegistration.getQuote().getContent());
    }

    private Quote findRandomQuote() {
        long count = quoteRepository.count();
        int i = (int) ((Math.random() * count));
        Page<Quote> page = quoteRepository.findAll(PageRequest.of(i, 1));
        Quote quote = page.getContent().get(0);
        return quote;
    }

    public List<GetQuotedexResponse> getQuotedex() {
        User user = getAuthenticatedUser();
        Set<QuoteRegistration> quoteRegistrationSet = quoteRegistrationRepository.findAllByUser(user);
        var possessedQuotes = quoteRegistrationSet.stream()
                .map(QuoteRegistration::getQuote)
                .collect(Collectors.toSet());

        return quoteRepository.findAll().stream()
                .map(quote -> mapQuotedexQuote(possessedQuotes, quote))
                .toList();

    }

    private static GetQuotedexResponse mapQuotedexQuote(Set<Quote> possessedQuotes, Quote quote) {
        var builder = GetQuotedexResponse.builder();
        builder.id(quote.getId());
        if (possessedQuotes.contains(quote)) {
            builder.isPossessed(true)
                    .originator(quote.getOriginator())
                    .content(quote.getContent());
        } else {
            builder.isPossessed(false);
        }
        return builder.build();
    }
}
