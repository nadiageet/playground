package com.example.playground.config;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Component
public class LoggingFilter extends CommonsRequestLoggingFilter {

    public LoggingFilter() {
        setIncludeHeaders(true);
        setIncludeQueryString(true);
        setIncludePayload(true);
        setMaxPayloadLength(2048);
    }
}
