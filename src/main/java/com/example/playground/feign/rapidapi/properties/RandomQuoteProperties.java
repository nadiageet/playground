package com.example.playground.feign.rapidapi.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "services.rapid-api.quotes")
public class RandomQuoteProperties {

    private String url;
    private String apiKey;
    private String host;

    public void setUrl(String url) {
        this.url = url;
    }


    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }


    public void setHost(String host) {
        this.host = host;
    }
}
