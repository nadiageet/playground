package com.example.playground.quote.service;

import com.example.playground.feign.rapidapi.RandomQuote;
import com.example.playground.feign.rapidapi.RandomQuoteClient;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.repository.QuoteRegistrationRepository;
import com.example.playground.quote.repository.QuoteRepository;
import com.example.playground.user.User;
import com.example.playground.user.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class QuoteService {

    private final RandomQuoteClient randomQuoteClient;
    private final QuoteRepository quoteRepository;

    private final UserRepository userRepository;


    private final QuoteRegistrationRepository quoteRegistrationRepository;


    public QuoteService(RandomQuoteClient randomQuoteClient, QuoteRepository quoteRepository, UserRepository userRepository, QuoteRegistrationRepository quoteRegistrationRepository) {
        this.randomQuoteClient = randomQuoteClient;
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
        this.quoteRegistrationRepository = quoteRegistrationRepository;
    }

    @Async
    public void generateQuote(Integer generationNumber) {

        for (int i = 0; i < generationNumber; i++) {
            generateQuoteFromAPI();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Quote generateQuoteFromAPI() {
        RandomQuote randomQuote = randomQuoteClient.getRandomQuote();
        Quote quote = new Quote();
        quote.setContent(randomQuote.content);
        quote.setOriginator(randomQuote.originator.name);
        return quoteRepository.save(quote);
    }

    public Set<Quote> getAllQuotes() {
        User user = getAuthenticatedUser();

        return userRepository.findAllQuotesByUser(user);
    }

    public Optional<Quote> getQuoteByID(Long quoteId) {
        return quoteRepository.findById(quoteId);

    }

    public void proposeQuote(Long quoteId) {
        QuoteRegistration quote = quoteRegistrationRepository.getReferenceById(quoteId);
        quote.setProposedQuote(true);
    }


    public void acceptQuote(Long quoteId) {

        quoteRepository.findById(quoteId);
    }

    private User getAuthenticatedUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("user %s was not found in database".formatted(userName)));
    }

    public void createQuote(String content) {
        Quote quote = new Quote();
        quote.setContent(content);
        quote.setOriginator(SecurityContextHolder.getContext().getAuthentication().getName());
        quoteRepository.save(quote);
    }
}
