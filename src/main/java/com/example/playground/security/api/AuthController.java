package com.example.playground.security.api;


import com.example.playground.UserRepository;
import com.example.playground.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    
    private final PasswordEncoder passwordEncoder;
    
    private final JwtUtils jwtUtils;
    
    private final UserRepository userRepository; 

    public AuthController(AuthenticationManager authenticationManager, 
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }


    @PostMapping(value = "/login")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return new JwtResponse(jwt,
                userDetails.getUsername(),
                roles);
    }

    @PostMapping(value = "/register")
    public void registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        var appUser = new com.example.playground.User();
        appUser.setUserName(registerRequest.getUserName());
        appUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        appUser.setRoles(Set.of(registerRequest.getRole()));
        
        userRepository.save(appUser);
    }
    
}
