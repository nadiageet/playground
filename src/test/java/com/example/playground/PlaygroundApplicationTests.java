package com.example.playground;

import com.example.playground.feign.rapidapi.RandomQuote;
import com.example.playground.feign.rapidapi.RandomQuoteClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PlaygroundApplicationTests {
    
    @Autowired
    MockMvc mockMvc;
    
    @MockBean
    RandomQuoteClient randomQuoteClient;

    @Test
    @WithMockUser(roles = "ADMIN")
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

}
