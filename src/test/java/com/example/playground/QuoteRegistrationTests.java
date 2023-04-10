package com.example.playground;

import com.example.playground.feign.rapidapi.RandomQuote;
import com.example.playground.feign.rapidapi.RandomQuoteClient;
import com.example.playground.quote.domain.Quote;
import com.example.playground.quote.domain.QuoteRegistration;
import com.example.playground.quote.repository.QuoteRegistrationRepository;
import com.example.playground.user.model.User;
import com.example.playground.user.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class QuoteRegistrationTests {
    @Autowired
    QuoteRegistrationRepository quoteRegistrationRepository;


    @Autowired
    MockMvc mockMvc;

    @MockBean
    RandomQuoteClient randomQuoteClient;

    @Autowired
    EntityManager entityManager;

    User nadia = new User();

    @Autowired
    private PasswordEncoder passwordEncoder;

    QuoteRegistrationTests() {
    }

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
    void giveRandomQuote() throws Exception {
        Quote possessedBefore = new Quote();
        possessedBefore.setContent("il fait beau");
        possessedBefore.setOriginator(nadia.getUserName());
        QuoteRegistration quoteRegistration = new QuoteRegistration();
        quoteRegistration.setQuote(possessedBefore);
        quoteRegistration.setUser(nadia);
        nadia.addRegistration(quoteRegistration);
        entityManager.persist(nadia);
        entityManager.persist(possessedBefore);

        Quote quoteNotPossessedAtStart = new Quote();
        quoteNotPossessedAtStart.setContent("le soir ne va tarder");
        entityManager.persist(quoteNotPossessedAtStart);

        mockMvc.perform(post("/api/v1/quoteRegistration/random"))
                .andExpect(status().isOk());

        assertThat(nadia.getQuoteRegistrations())
                .map(QuoteRegistration::getQuote)
                .contains(possessedBefore, quoteNotPossessedAtStart);

    }

}
