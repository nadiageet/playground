package com.example.playground.quote.service;

import com.example.playground.exception.ApplicationException;
import com.example.playground.feign.rapidapi.RandomQuote;
import com.example.playground.feign.rapidapi.RandomQuoteClient;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.domain.QuoteTrade;
import com.example.playground.quote.domain.TradeStatus;
import com.example.playground.quote.repository.QuoteRegistrationRepository;
import com.example.playground.quote.repository.QuoteRepository;
import com.example.playground.quote.repository.QuoteToTradeRepository;
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

    private final QuoteToTradeRepository quoteToTradeRepository;


    public QuoteService(RandomQuoteClient randomQuoteClient, QuoteRepository quoteRepository, UserRepository userRepository, QuoteRegistrationRepository quoteRegistrationRepository, QuoteToTradeRepository quoteToTradeRepository) {
        this.randomQuoteClient = randomQuoteClient;
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
        this.quoteRegistrationRepository = quoteRegistrationRepository;
        this.quoteToTradeRepository = quoteToTradeRepository;
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

    public void tradeQuote(Long quoteRegistrationId, Long quoteInitiatorId) {
        QuoteRegistration quoteRegistration = quoteRegistrationRepository.findById(quoteRegistrationId)
                .orElseThrow(() -> new ApplicationException());

        if (!quoteRegistration.isProposedQuote()) {
            throw new ApplicationException("tradeNotProPosed");
        }

        QuoteTrade trade = new QuoteTrade();
        User authenticatedUser = getAuthenticatedUser();
        trade.setUserInitiator(authenticatedUser);
        trade.setQuote(quoteRegistration.getQuote());
        trade.setUserValidator(quoteRegistration.getUser());
        
        if(quoteInitiatorId != null){
            Quote quoteInitiator = quoteRepository.getReferenceById(quoteInitiatorId);
            boolean userPossedQuote = trade.getUserInitiator().getQuoteRegistrations()
                    .stream().map(QuoteRegistration::getQuote)
                    .anyMatch(quote -> quote.equals(quoteInitiator));
            if(!userPossedQuote){
                throw new ApplicationException("forbidden");
            }
            trade.setQuoteInitiator(quoteInitiator);
        }

        quoteToTradeRepository.save(trade);
    }

    public void updateTrade(Long tradeId, TradeStatus status) {
        QuoteTrade trade = quoteToTradeRepository.getReferenceById(tradeId);
        
        Quote quote = trade.getQuote();
        User authenticatedUser = getAuthenticatedUser();
        if (!trade.getUserValidator().equals(authenticatedUser)) {
            throw new ApplicationException("forbidden");
        }
        if (trade.getStatus() != TradeStatus.WAITING) {
            throw new ApplicationException("not_waiting");
        }

        trade.setStatus(status);
        if (status == TradeStatus.ACCEPTED) {

            // nouvelle acquisition
            addQuoteToUser(quote, trade.getUserInitiator());

            // enlever l'ancienne
            removeQuoteFromUser(quote, trade.getUserValidator());
        }
    }

    private static void addQuoteToUser(Quote quote, User userInitiator) {
        QuoteRegistration newRegistration = new QuoteRegistration();
        newRegistration.setQuote(quote);
        newRegistration.setProposedQuote(false);
        userInitiator.addRegistration(newRegistration);
    }

    private void removeQuoteFromUser(Quote quote, User user) {

        QuoteRegistration q = user.getQuoteRegistrations().stream()
                .filter(quoteRegistration -> quoteRegistration.getQuote().equals(quote))
                .findAny()
                .orElseThrow();
        user.removeRegistration(q);
    }
}
