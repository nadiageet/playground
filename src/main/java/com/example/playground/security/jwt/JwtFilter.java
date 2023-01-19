package com.example.playground.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {


    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String headerAuth = request.getHeader("Authorization");

        log.debug("intercepted request received : {}", request.getRequestURI());
        if (headerAuth == null) {
            // if no Authorization header, do not try to authenticate with JWT
            log.debug("no jwt token found in request, continuing security filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = extractTokenFromAuthorizationHeader(headerAuth);
            log.debug("jwt token found in request");
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                log.debug("extracting username {} from jwt token", username);
                authenticate(request, username);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e);
        }

    }

    private void authenticate(HttpServletRequest request, String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("user {} authenticated", username);
    }

    private String extractTokenFromAuthorizationHeader(String headerAuth) {
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        } else {
            return null;
        }
    }


}
