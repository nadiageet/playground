package com.example.playground.quote.service;

import com.example.playground.quote.api.response.GetCountQuotedexResponse;
import com.example.playground.quote.api.response.GetQuotedexResponse;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.repository.QuoteRegistrationRepository;
import com.example.playground.quote.repository.QuoteRepository;
import com.example.playground.quote.repository.UserRepository;
import com.example.playground.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
        User authenticatedUser = getAuthenticatedUser();
        findRandomQuote(authenticatedUser).ifPresent(quote -> registerQuoteForUser(authenticatedUser, quote));

    }

    private void registerQuoteForUser(User authenticatedUser, Quote quote) {
        QuoteRegistration quoteRegistration = new QuoteRegistration();
        quoteRegistration.setQuote(quote);
        authenticatedUser.addRegistration(quoteRegistration);

        quoteRegistrationRepository.save(quoteRegistration);
        log.info("user {} has a new quote : {}", authenticatedUser.getUserName(), quoteRegistration.getQuote().getContent());
    }

    private Optional<Quote> findRandomQuote(User user) {
        List<Long> quotes = quoteRepository.findQuotesNotPossessedByUser(user);
        if (quotes.isEmpty()) {
            return Optional.empty();
        }
        long count = quotes.size();
        int i = (int) ((Math.random() * count));
        return Optional.of(quoteRepository.getReferenceById(quotes.get(i)));
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

    public List<GetCountQuotedexResponse> getCountQuotedex() {
        User user = getAuthenticatedUser();
        Set<QuoteRegistration> quoteRegistrationSet = quoteRegistrationRepository.findAllByUser(user);

        return quoteRepository.findAll().stream()
                .map(quote -> mapCountQuotedex(quoteRegistrationSet, quote))
                .toList();

    }

    public GetCountQuotedexResponse mapCountQuotedex(Set<QuoteRegistration> possessedQuotes, Quote quote) {
        var builder = GetCountQuotedexResponse.builder();
        builder.id(quote.getId());
        int countOfQuote = (int) possessedQuotes.stream().filter(quoteRegistration -> quoteRegistration.getQuote().equals(quote))
                .count();
        builder.numberOfQuote(countOfQuote);
        if (countOfQuote > 0) {
            builder.originator(quote.getOriginator())
                    .content(quote.getContent());
        }
        return builder.build();
    }

    public GetQuotedexResponse receiveQuote() {
        long count = quoteRepository.count();
        Quote quoteReceived = quoteRepository.findAll(PageRequest.of((int) (Math.random() * count), 1)).getContent().get(0);
        QuoteRegistration quoteRegistration = new QuoteRegistration();
        quoteRegistration.setQuote(quoteReceived);
        getAuthenticatedUser().addRegistration(quoteRegistration);
        return new GetQuotedexResponse(
                quoteReceived.getId(),
                quoteReceived.getContent(),
                quoteReceived.getOriginator(),
                quoteRegistration.isProposedQuote()
        );
    }
}
