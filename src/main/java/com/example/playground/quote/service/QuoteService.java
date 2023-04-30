package com.example.playground.quote.service;

import com.example.playground.exception.ApplicationException;
import com.example.playground.feign.rapidapi.RandomQuote;
import com.example.playground.feign.rapidapi.RandomQuoteClient;
import com.example.playground.mapper.QuoteMapper;
import com.example.playground.mapper.UserMapper;
import com.example.playground.quote.api.response.*;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.domain.QuoteTrade;
import com.example.playground.quote.domain.TradeStatus;
import com.example.playground.quote.repository.QuoteRegistrationRepository;
import com.example.playground.quote.repository.QuoteRepository;
import com.example.playground.quote.repository.QuoteToTradeRepository;
import com.example.playground.quote.repository.UserRepository;
import com.example.playground.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class QuoteService {

    private final RandomQuoteClient randomQuoteClient;
    private final QuoteMapper quoteMapper;
    private final QuoteRepository quoteRepository;

    private final UserRepository userRepository;


    private final QuoteRegistrationRepository quoteRegistrationRepository;

    private final QuoteToTradeRepository quoteToTradeRepository;
    
    private final UserMapper userMapper;


    public QuoteService(RandomQuoteClient randomQuoteClient,
                        QuoteMapper quoteMapper,
                        QuoteRepository quoteRepository,
                        UserRepository userRepository,
                        QuoteRegistrationRepository quoteRegistrationRepository,
                        QuoteToTradeRepository quoteToTradeRepository, UserMapper userMapper) {
        this.randomQuoteClient = randomQuoteClient;
        this.quoteMapper = quoteMapper;
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
        this.quoteRegistrationRepository = quoteRegistrationRepository;
        this.quoteToTradeRepository = quoteToTradeRepository;
        this.userMapper = userMapper;
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

    public void proposeQuote(Long quoteRegistrationId, boolean proposed) {
        QuoteRegistration quote = quoteRegistrationRepository.findById(quoteRegistrationId)
                .orElseThrow(() -> new ApplicationException("quote-registration.notfound"));
        quote.setProposedQuote(proposed);
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
        addQuoteToUser(quote, getAuthenticatedUser());
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
        trade.setQuoteValidator(quoteRegistration.getQuote());
        trade.setUserValidator(quoteRegistration.getUser());

        if (quoteInitiatorId != null) {
            Quote quoteInitiator = quoteRepository.getReferenceById(quoteInitiatorId);
            boolean userPossedQuote = trade.getUserInitiator().getQuoteRegistrations()
                    .stream().map(QuoteRegistration::getQuote)
                    .anyMatch(quote -> quote.equals(quoteInitiator));
            if (!userPossedQuote) {
                throw new ApplicationException("forbidden");
            }
            trade.setQuoteInitiator(quoteInitiator);
        }

        quoteToTradeRepository.save(trade);
    }

    public void updateTrade(Long tradeId, TradeStatus status) {
        QuoteTrade trade = quoteToTradeRepository.getReferenceById(tradeId);

        Quote quoteValidator = trade.getQuoteValidator();
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
            addQuoteToUser(quoteValidator, trade.getUserInitiator());

            // enlever l'ancienne
            removeQuoteFromUser(quoteValidator, trade.getUserValidator());

            trade.getQuoteInitiator().ifPresent(quoteInitiator -> {
                // nouvelle acquisition
                addQuoteToUser(quoteInitiator, trade.getUserValidator());

                // enlever l'ancienne
                removeQuoteFromUser(quoteInitiator, trade.getUserInitiator());
            });
        } else if (status == TradeStatus.REFUSED) {
            trade.setStatus(TradeStatus.REFUSED);
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

    public List<TradeHistory> getTadeHistory() {
        User authenticatedUser = getAuthenticatedUser();
        return quoteToTradeRepository.getAllTradeAcceptedByUser(authenticatedUser)
                .stream().map(quoteTrade -> {
                    boolean isValidator = quoteTrade.getUserValidator().equals(authenticatedUser);

                    TradeHistory tradeHistory = new TradeHistory();

                    // user avec lequel on fait l'echange 
                    tradeHistory.setTraderName(quoteTrade.getUserInitiator().getUserName());

                    Quote quoteReceived = isValidator ? quoteTrade.getQuoteInitiator().orElse(null) : quoteTrade.getQuoteValidator();
                    tradeHistory.setQuoteReceived(quoteMapper.mapToQuoteTrade(quoteReceived));

                    Quote quoteGiven = isValidator ? quoteTrade.getQuoteValidator() : quoteTrade.getQuoteInitiator().orElse(null);
                    tradeHistory.setQuoteGiven(quoteMapper.mapToQuoteTrade(quoteGiven));
                    return tradeHistory;

                }).toList();
    }

    public List<TradeInProgress> getTradeInProgress() {
        return quoteToTradeRepository.getAllTradeInProposedStatus()
                .stream().map(trade -> {

                    TradeInProgress tradeInProgress = new TradeInProgress();

                    QuoteTradeResponse initiatorQuote = trade.getQuoteInitiator()
                            .map(quoteMapper::mapToQuoteTrade)
                            .orElse(null);
                    
                    TradePart initiatorPart = new TradePart(initiatorQuote,
                            trade.getUserInitiator().getUserName());
                    tradeInProgress.setInitiator(initiatorPart);

                    QuoteTradeResponse quoteValidator = quoteMapper.mapToQuoteTrade(trade.getQuoteValidator());
                    TradePart validatorPart = new TradePart(quoteValidator, trade.getUserValidator().getUserName());
                    tradeInProgress.setValidator(validatorPart);
                    
                    return tradeInProgress;
                }).toList();
    }

    public Page<UserResponse> findAllUsers(Pageable pageable) {
       Page<User> users = userRepository.findAll(pageable);

       return users.map(userMapper::mapToUserResponse);
       
    }

    public void giftQuote(Long userId, Long quoteId) {
        QuoteRegistration quoteRegistration = new QuoteRegistration();
        quoteRegistration.setQuote(quoteRepository.getReferenceById(quoteId));
        User user = userRepository.getReferenceById(userId);
        user.addRegistration(quoteRegistration);
    }

}
