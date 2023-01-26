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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PlaygroundApplicationTests {

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
    @WithMockUser(roles = "ADMIN", username = "guigui")
    void getContent() throws Exception {

        // GIVEN
        RandomQuote response = new RandomQuote();
        response.id = 1;
        response.content = "The music is not in the notes, but in the silence between.";
        Mockito.when(randomQuoteClient.getRandomQuote())
                .thenReturn(response);

        // WHEN
        String quote = mockMvc.perform(get("/api/v1/quote"))
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

        assertThat(trade.getQuoteRegistration()).isSameAs(quoteRegistration);
        assertThat(trade.getUserInitiator()).isSameAs(nadia);
        assertThat(trade.getStatus()).isEqualTo(TradeStatus.WAITING);
        assertThat(trade.getQuoteRegistration().getQuote().getContent()).isEqualTo("le repas du mmidi été bon");

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
        trade.setUserInitiator(guillaume);
        trade.setQuoteRegistration(quoteRegistration);
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

        assertThat(trade.getQuoteRegistration()).isSameAs(quoteRegistration);
        assertThat(trade.getUserInitiator()).isSameAs(guillaume);
        assertThat(trade.getStatus()).isEqualTo(TradeStatus.ACCEPTED);
        assertThat(trade.getQuoteRegistration()).isNull();

        QuoteRegistration newQuoteRegistration = entityManager.createQuery("select qr from QuoteRegistration qr", QuoteRegistration.class)
                .getSingleResult();

        assertThat(newQuoteRegistration.getQuote().getContent()).isEqualTo("le repas du mmidi été bon");

    }

}
