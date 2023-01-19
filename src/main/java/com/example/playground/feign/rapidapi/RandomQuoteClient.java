package com.example.playground.feign.rapidapi;

import com.example.playground.feign.rapidapi.properties.RandomQuoteProperties;
import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        url = "${infra.quote.url:NO_VALID_URL}",
        name = "quote",
        decode404 = true,
        configuration = RandomQuoteClient.Configuration.class
)
public interface RandomQuoteClient {

    @GetMapping("/quotes/random/")
    RandomQuote getRandomQuote();
    
     class Configuration {

         @Bean
         RequestInterceptor auth(RandomQuoteProperties properties) {
             return requestTemplate -> requestTemplate
                     .header("X-RapidAPI-Key", properties.getApiKey())
                     .header("X-RapidAPI-Host", properties.getHost());
         }
         
    }
}
