package com.example.playground;

import com.example.playground.feign.rapidapi.RandomQuoteClient;
import com.example.playground.quote.api.response.TradeInProgress;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.domain.QuoteTrade;
import com.example.playground.quote.domain.TradeStatus;
import com.example.playground.quote.repository.QuoteRegistrationRepository;
import com.example.playground.user.User;
import com.example.playground.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TradeQuoteTests {
    @Autowired
    QuoteRegistrationRepository quoteRegistrationRepository;


    @Autowired
    MockMvc mockMvc;

    @MockBean
    RandomQuoteClient randomQuoteClient;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;
    User nadia = new User();
    User guigui = new User();


    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

        nadia.setUserName("nadia");
        nadia.setRoles(Set.of(UserRole.ADMIN));
        nadia.setPassword(passwordEncoder.encode("pass"));

        entityManager.persist(nadia);
        guigui.setUserName("guigui");
        guigui.setRoles(Set.of(UserRole.COLLECTOR));
        guigui.setPassword(passwordEncoder.encode("pass"));

        Quote quoteValidator = new Quote();
        quoteValidator.setContent("le repas du midi été bon");
        quoteValidator.setOriginator(guigui.getUserName());
        entityManager.persist(quoteValidator);

        QuoteRegistration quoteRegistrationValidator = new QuoteRegistration();
        quoteRegistrationValidator.setQuote(quoteValidator);
        quoteRegistrationValidator.setProposedQuote(true);
        guigui.addRegistration(quoteRegistrationValidator);

        Quote quoteInitiator = new Quote();
        quoteInitiator.setContent("le repas du midi été mouvementé");
        entityManager.persist(quoteInitiator);

        QuoteRegistration quoteRegistrationInitiator = new QuoteRegistration();
        quoteRegistrationInitiator.setQuote(quoteInitiator);
        quoteRegistrationInitiator.setProposedQuote(true);
        nadia.addRegistration(quoteRegistrationInitiator);

        entityManager.persist(nadia);

        entityManager.persist(guigui);

    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "nadia")
    void getTradeInProgress() throws Exception {
        // GIVEN
        QuoteTrade quoteTradeInitiator = new QuoteTrade();
        quoteTradeInitiator.setUserInitiator(nadia);
        quoteTradeInitiator.setUserValidator(guigui);

        quoteTradeInitiator.setStatus(TradeStatus.WAITING);
        quoteTradeInitiator.setQuoteInitiator(nadia.getQuoteRegistrations().iterator().next().getQuote());
        quoteTradeInitiator.setQuoteValidator(guigui.getQuoteRegistrations().iterator().next().getQuote());
        entityManager.persist(quoteTradeInitiator);

        QuoteTrade quoteTradeValidator = new QuoteTrade();
        quoteTradeValidator.setUserInitiator(guigui);
        quoteTradeValidator.setUserValidator(nadia);
        quoteTradeValidator.setStatus(TradeStatus.WAITING);
        quoteTradeValidator.setQuoteInitiator(guigui.getQuoteRegistrations().iterator().next().getQuote());
        quoteTradeValidator.setQuoteValidator(nadia.getQuoteRegistrations().iterator().next().getQuote());
        entityManager.persist(quoteTradeValidator);

        //When

        String json = mockMvc.perform(get("/api/v1/trade/tradeInProgress/"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TradeInProgress> trades = Arrays.asList(objectMapper.readValue(json, TradeInProgress[].class));

        assertThat(trades).hasSize(2);
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "nadia")
    void giftQuote() throws Exception {
        
        Quote quote = new Quote();
        quote.setContent("le repas du mmidi été bon");
        quote.setOriginator(guigui.getUserName());
        entityManager.persist(quote);
        
        mockMvc.perform(post("/api/v1/gift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "quoteId": "%s",
                                "userId": "%s"
                                }
                                """.formatted(quote.getId(), guigui.getId()))
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(guigui.getQuoteRegistrations())
                .map(QuoteRegistration::getQuote)
                .contains(quote);


    }

}
