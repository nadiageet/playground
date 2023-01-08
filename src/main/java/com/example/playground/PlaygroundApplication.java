package com.example.playground;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@SpringBootApplication
@RestController
@EnableFeignClients
@EnableConfigurationProperties
@EnableJpaRepositories
@Slf4j
public class PlaygroundApplication {


    public static void main(String[] args) {
        SpringApplication.run(PlaygroundApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,
                                        QuoteService quoteService,
                                        PasswordEncoder passwordEncoder) {
        return args -> {
            log.info("Creation of user admin with password 'pass'");
            User user = new User();
            user.setUserName("admin");
            user.setRoles(Set.of(UserRole.ADMIN));
            user.setPassword(passwordEncoder.encode("pass"));

            User guillaume = new User();
            guillaume.setUserName("guigui");
            guillaume.setRoles(Set.of(UserRole.COLLECTOR));
            guillaume.setPassword(passwordEncoder.encode("pass"));

            log.info("The user was saved with id {}", user.getId());
            Quote quote = quoteService.generateQuoteFromAPI();

            QuoteRegistration quoteRegistration = new QuoteRegistration();
            quoteRegistration.setQuote(quote);
            user.addRegistration(quoteRegistration);

            userRepository.save(user);
            userRepository.save(guillaume);
            log.info("Saving quote {} for the user", quote.getId());
        };
    }

}
