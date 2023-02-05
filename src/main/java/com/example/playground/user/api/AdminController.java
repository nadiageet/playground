package com.example.playground.user.api;

import com.example.playground.quote.api.response.UserResponse;
import com.example.playground.quote.service.QuoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final QuoteService quoteService;

    public AdminController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/users")
    public Page<UserResponse> getAllUsers(Pageable pageable){
        return quoteService.findAllUsers(pageable);
    }

}
