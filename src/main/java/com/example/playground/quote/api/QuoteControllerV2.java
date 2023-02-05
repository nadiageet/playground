package com.example.playground.quote.api;

import com.example.playground.quote.api.response.GetCountQuotedexResponse;
import com.example.playground.quote.service.QuoteRegistrationService;
import com.example.playground.quote.service.QuoteService;
import com.example.playground.quote.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: 18/01/2023  split this controller into multiple domains
@RestController
@Slf4j
@ControllerAdvice
@RequestMapping("/api/v2/")
public class QuoteControllerV2 {


    private final QuoteRegistrationService quoteRegistrationService;
    private final UserRepository userRepository;
    private final QuoteService quoteService;



    public QuoteControllerV2(QuoteRegistrationService quoteRegistrationService, UserRepository userRepository, QuoteService quoteService) {
        this.quoteRegistrationService = quoteRegistrationService;
        this.userRepository = userRepository;
        this.quoteService = quoteService;
    }


    @ApiOperation(value = "V2 version with count", tags = "Quotedex")
    @GetMapping("/quotedex")
    public List<GetCountQuotedexResponse> getQuotedex() {
        return quoteRegistrationService.getCountQuotedex();
    }

    

  


}
