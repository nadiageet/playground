package com.example.playground.quote.repository;

import com.example.playground.quote.domain.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
