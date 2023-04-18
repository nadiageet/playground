package com.example.playground.config;

import com.example.playground.gift.model.Gift;
import com.example.playground.gift.model.GiftType;
import com.example.playground.gift.repository.GiftRepository;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.repository.QuoteRepository;
import com.example.playground.quote.repository.UserRepository;
import com.example.playground.user.model.User;
import com.example.playground.user.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Slf4j
public class DataMockConfiguration {

    private static Quote createQuote(User user, String content) {
        Quote quote = new Quote();
        quote.setContent(content);
        quote.setOriginator(user.getUserName());
        QuoteRegistration quoteRegistration = new QuoteRegistration();
        quoteRegistration.setQuote(quote);
        user.addRegistration(quoteRegistration);
        return quote;
    }

    private static User createAdmin(PasswordEncoder passwordEncoder, String userName) {
        User user = new User();
        user.setUserName(userName);
        user.setRoles(Set.of(UserRole.ADMIN));
        user.setPassword(passwordEncoder.encode("pass"));
        return user;
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner commandLineRunner(UserRepository userRepository,
                                               QuoteRepository quoteRepository,
                                               GiftRepository giftRepository,

                                               PasswordEncoder passwordEncoder) {
        return args -> {
            log.info("Creation of user admin with password 'pass'");
            User admin = createAdmin(passwordEncoder, "admin");
            User guillaume = createCollector(passwordEncoder, "guigui");
            User toto = createCollector(passwordEncoder, "toto");


            Quote quote = createQuote(guillaume, "no rapidapi for now");
            quoteRepository.save(quote);

            Quote quote2 = createQuote(guillaume, "The theorem of Guillaume");
            quoteRepository.save(quote2);

            Quote quote3 = createQuote(toto, "La blague de Toto");
            quoteRepository.save(quote3);

            Quote quote4 = createQuote(admin, "Je suis le grand Admin");
            quoteRepository.save(quote4);


            userRepository.save(admin);
            userRepository.save(guillaume);
            userRepository.save(toto);


            for (int i = 0; i < 10; i++) {
                Gift gift = new Gift();
                gift.setType(GiftType.RANDOM_QUOTE);
                guillaume.addGift(gift);
                giftRepository.save(gift);
            }
        };
    }

    private User createCollector(PasswordEncoder passwordEncoder, String userName) {
        User user = new User();
        user.setUserName(userName);
        user.setRoles(Set.of(UserRole.COLLECTOR));
        user.setPassword(passwordEncoder.encode("pass"));
        return user;
    }

}
