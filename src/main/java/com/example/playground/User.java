package com.example.playground;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
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


    public void addRegistration(QuoteRegistration quoteRegistration) {
        quoteRegistration.setUser(this);
        quoteRegistrations.add(quoteRegistration);
    }
    
}
