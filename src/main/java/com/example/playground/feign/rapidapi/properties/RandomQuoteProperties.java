package com.example.playground.feign.rapidapi.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "infra.quote")
public class RandomQuoteProperties {
    
    private String url;
    private String apiKey;
    private String host;
    
}
