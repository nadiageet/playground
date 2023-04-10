package com.example.playground.user.service;

import com.example.playground.exception.NotFoundException;
import com.example.playground.quote.repository.UserRepository;
import com.example.playground.user.api.response.AccountInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AccountService {


    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AccountInfo getUserAccountInfoFromAuthentication() {
        log.info("getting account information");
        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserNameFetchRoles(userName)
                .map(u -> new AccountInfo(u.getUserName(), u.getRoles()))
                .orElseThrow(() -> new NotFoundException("user.notfound"));

    }
}
