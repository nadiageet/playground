package com.example.playground.config;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoggingFilter extends CommonsRequestLoggingFilter {

    public LoggingFilter() {
        setIncludeHeaders(true);
        setIncludeQueryString(true);
        setIncludePayload(true);
        setMaxPayloadLength(2048);
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return super.shouldLog(request) &&
               !request.getRequestURI().contains("/swagger-ui")
               && !request.getRequestURI().contains("api-docs")
               && !request.getRequestURI().contains("h2-console");
    }
}
