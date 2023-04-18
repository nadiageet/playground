package com.example.playground.user.model;

import com.example.playground.gift.model.Gift;
import com.example.playground.quote.domain.QuoteRegistration;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String userName;
    
    private String password;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<QuoteRegistration> quoteRegistrations = new HashSet<>();

    @ElementCollection(targetClass = UserRole.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Gift> gifts = new ArrayList<>();


    public void addRegistration(QuoteRegistration quoteRegistration) {
        quoteRegistration.setUser(this);
        quoteRegistrations.add(quoteRegistration);
    }

    public void removeRegistration(QuoteRegistration quoteRegistration) {
        quoteRegistration.setUser(null);
        quoteRegistrations.remove(quoteRegistration);
    }

    public void addGift(Gift gift) {
        gifts.add(gift);
        gift.setUser(this);
    }

}
