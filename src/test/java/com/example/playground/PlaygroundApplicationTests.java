package com.example.playground;

import com.example.playground.feign.rapidapi.RandomQuote;
import com.example.playground.feign.rapidapi.RandomQuoteClient;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.domain.QuoteTrade;
import com.example.playground.quote.domain.TradeStatus;
import com.example.playground.user.User;
import com.example.playground.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PlaygroundApplicationTests {

    public static final String GUIGUI = "guigui";
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RandomQuoteClient randomQuoteClient;

    @Autowired
    EntityManager entityManager;

    User nadia = new User();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

        nadia.setUserName("nadia");
        nadia.setRoles(Set.of(UserRole.ADMIN));
        nadia.setPassword(passwordEncoder.encode("pass"));

        entityManager.persist(nadia);

    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "nadia")
    void getContent() throws Exception {

        // GIVEN
        RandomQuote response = new RandomQuote();
        response.id = 1;
        response.content = "The music is not in the notes, but in the silence between.";
        Mockito.when(randomQuoteClient.getRandomQuote())
                .thenReturn(response);

        // WHEN
        String quote = mockMvc.perform(get("/api/v1/quote/random"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(quote).isEqualTo("The music is not in the notes, but in the silence between.");

    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "nadia")
    void tradeQuote() throws Exception {
        // GIVEN
        User guillaume = new User();
        guillaume.setUserName("guigui");
        guillaume.setRoles(Set.of(UserRole.COLLECTOR));
        guillaume.setPassword(passwordEncoder.encode("pass"));
        Quote quote = new Quote();
        quote.setContent("le repas du mmidi été bon");
        quote.setOriginator(guillaume.getUserName());
        entityManager.persist(quote);

        QuoteRegistration quoteRegistration = new QuoteRegistration();
        quoteRegistration.setQuote(quote);
        quoteRegistration.setProposedQuote(true);
        guillaume.addRegistration(quoteRegistration);

        entityManager.persist(guillaume);
        // WHEN
        mockMvc.perform(post("/api/v1/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "quoteRegistrationId": "%s"
                                }
                                """.formatted(quoteRegistration.getId()))
                )
                .andExpect(status().isOk());

        QuoteTrade trade = entityManager.createQuery("select t from QuoteTrade t", QuoteTrade.class)
                .getSingleResult();

        assertThat(trade.getQuoteValidator()).isSameAs(quote);
        assertThat(trade.getUserInitiator()).isSameAs(nadia);
        assertThat(trade.getStatus()).isEqualTo(TradeStatus.WAITING);
        assertThat(trade.getQuoteValidator().getContent()).isEqualTo("le repas du mmidi été bon");

    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "nadia")
    void updateTrade() throws Exception {
        // GIVEN
        User guillaume = new User();
        guillaume.setUserName("guigui");
        guillaume.setRoles(Set.of(UserRole.COLLECTOR));
        guillaume.setPassword(passwordEncoder.encode("pass"));
        Quote quote = new Quote();
        quote.setContent("le repas du mmidi été bon");
        quote.setOriginator(guillaume.getUserName());
        entityManager.persist(quote);

        QuoteRegistration quoteRegistration = new QuoteRegistration();
        quoteRegistration.setQuote(quote);
        quoteRegistration.setProposedQuote(true);
        nadia.addRegistration(quoteRegistration);

        entityManager.persist(guillaume);

        QuoteTrade trade = new QuoteTrade();
        trade.setUserValidator(nadia);
        trade.setUserInitiator(guillaume);
        trade.setQuoteValidator(quote);
        entityManager.persist(trade);
        // WHEN
        mockMvc.perform(put("/api/v1/trade/" + trade.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "status": "%s"
                                }
                                """.formatted("ACCEPTED")
                        )
                )
                .andExpect(status().isOk());

        entityManager.flush();

        assertThat(trade.getQuoteValidator()).isSameAs(quote);
        assertThat(trade.getUserInitiator()).isSameAs(guillaume);
        assertThat(trade.getStatus()).isEqualTo(TradeStatus.ACCEPTED);

        assertThat(trade.getUserValidator().getQuoteRegistrations())
                .noneMatch(r -> r.getQuote().equals(quote));

        QuoteRegistration newQuoteRegistration = entityManager.createQuery("select qr from QuoteRegistration qr", QuoteRegistration.class)
                .getSingleResult();

        assertThat(newQuoteRegistration.getQuote().getContent()).isEqualTo("le repas du mmidi été bon");

    }

    @Nested
    @DisplayName("when guillaume and nadia trade together two quotes")
    class HavingATwoWayTrade {

        private User guillaume;
        private QuoteRegistration quoteRegistrationValidator;
        private Quote quoteValidator;
        private Quote quoteInitiator;
        private QuoteRegistration quoteRegistrationInitiator;

        @BeforeEach
        void setUp() {
            guillaume = new User();
            guillaume.setUserName(GUIGUI);
            guillaume.setRoles(Set.of(UserRole.COLLECTOR));
            guillaume.setPassword(passwordEncoder.encode("pass"));

            quoteValidator = new Quote();
            quoteValidator.setContent("le repas du midi été bon");
            quoteValidator.setOriginator(guillaume.getUserName());
            entityManager.persist(quoteValidator);

            quoteRegistrationValidator = new QuoteRegistration();
            quoteRegistrationValidator.setQuote(quoteValidator);
            quoteRegistrationValidator.setProposedQuote(true);
            guillaume.addRegistration(quoteRegistrationValidator);

            quoteInitiator = new Quote();
            quoteInitiator.setContent("le repas du midi été mouvementé");
            entityManager.persist(quoteInitiator);

            quoteRegistrationInitiator = new QuoteRegistration();
            quoteRegistrationInitiator.setQuote(quoteInitiator);
            quoteRegistrationInitiator.setProposedQuote(true);
            nadia.addRegistration(quoteRegistrationInitiator);

            entityManager.persist(nadia);

            entityManager.persist(guillaume);
        }

        @Test
        @WithMockUser(roles = "ADMIN", username = "nadia")
        @DisplayName("on trade creation should start trade")
        void tradeQuoteInTooWay() throws Exception {
            // GIVEN

            // WHEN
            mockMvc.perform(post("/api/v1/trade")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "quoteRegistrationId": "%s",
                                    "quoteInitiatorId": "%s"
                                    }
                                    """.formatted(quoteRegistrationValidator.getId(), quoteInitiator.getId()))
                    )
                    .andExpect(status().isOk());

            QuoteTrade trade = entityManager.createQuery("select t from QuoteTrade t", QuoteTrade.class)
                    .getSingleResult();

            assertThat(trade.getQuoteInitiator()).hasValue(quoteInitiator);
            assertThat(trade.getUserInitiator()).isSameAs(nadia);

            assertThat(trade.getQuoteValidator()).isSameAs(quoteValidator);
            assertThat(trade.getUserValidator()).isSameAs(guillaume);

            assertThat(trade.getStatus()).isEqualTo(TradeStatus.WAITING);
            assertThat(trade.getQuoteValidator().getContent()).isEqualTo("le repas du midi été bon");

        }

        @Test
        @WithMockUser(roles = "ADMIN", username = GUIGUI)
        @DisplayName("on trade validation, should exchange quotes")
        void shouldAcceptTrade() throws Exception {
            // GIVEN
            QuoteTrade quoteTrade = new QuoteTrade();
            quoteTrade.setUserInitiator(nadia);
            quoteTrade.setUserValidator(guillaume);
            quoteTrade.setQuoteInitiator(nadia.getQuoteRegistrations().iterator().next().getQuote());
            quoteTrade.setQuoteValidator(guillaume.getQuoteRegistrations().iterator().next().getQuote());
            entityManager.persist(quoteTrade);

            //When

            String url = "/api/v1/trade/" + quoteTrade.getId();
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                            "status": "ACCEPTED"
                            }
                            """)
            ).andExpect(status().isOk());


            assertThat(quoteTrade.getStatus()).isEqualTo(TradeStatus.ACCEPTED);

            Optional<Quote> quoteInitiatorToValidator = guillaume.getQuoteRegistrations().stream()
                    .map(QuoteRegistration::getQuote)
                    .filter(quote -> quote.equals(quoteInitiator))
                    .findAny();
            
            Optional<Quote> quoteValidatorToInitiator = nadia.getQuoteRegistrations().stream()
                    .map(QuoteRegistration::getQuote)
                    .filter(quote -> quote.equals(quoteValidator))
                    .findAny();

            assertThat(quoteInitiatorToValidator).isPresent();
            assertThat(quoteValidatorToInitiator).isPresent();

            assertThat(guillaume.getQuoteRegistrations()).doesNotContain(quoteRegistrationValidator);
            assertThat(nadia.getQuoteRegistrations()).doesNotContain(quoteRegistrationInitiator);

        }
        @Test
        @WithMockUser(roles = "ADMIN", username = GUIGUI)
        @DisplayName("on trade validation, should not exchange quotes")
        void shouldNotAcceptTrade() throws Exception {
            // GIVEN
            QuoteTrade quoteTrade = new QuoteTrade();
            quoteTrade.setUserInitiator(nadia);
            quoteTrade.setUserValidator(guillaume);
            quoteTrade.setQuoteInitiator(nadia.getQuoteRegistrations().iterator().next().getQuote());
            quoteTrade.setQuoteValidator(guillaume.getQuoteRegistrations().iterator().next().getQuote());
            entityManager.persist(quoteTrade);

            //When

            String url = "/api/v1/trade/" + quoteTrade.getId();
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                            "status": "REFUSED"
                            }
                            """)
            ).andExpect(status().isOk());


            assertThat(quoteTrade.getStatus()).isEqualTo(TradeStatus.REFUSED);

            assertThat(guillaume.getQuoteRegistrations()).contains(quoteRegistrationValidator);
            assertThat(nadia.getQuoteRegistrations()).contains(quoteRegistrationInitiator);

        }


    }

}
