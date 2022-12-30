package com.example.playground;

import com.example.playground.feign.RandomQuote;
import com.example.playground.feign.RandomQuoteClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PlaygroundApplicationTests {
    
    @Autowired
    MockMvc mockMvc;
    
    @MockBean
    RandomQuoteClient randomQuoteClient;
    
    @Test
    void getContent() throws Exception {

        // GIVEN
        RandomQuote response = new RandomQuote();
        response.content = "The music is not in the notes, but in the silence between.";
        Mockito.when(randomQuoteClient.getRandomQuote())
                .thenReturn(response);
        
        // WHEN
        String quote = mockMvc.perform(get("/quote"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(quote).isEqualTo("The music is not in the notes, but in the silence between.");

    }

}
