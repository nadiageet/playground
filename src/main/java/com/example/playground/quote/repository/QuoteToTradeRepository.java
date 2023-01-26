package com.example.playground.quote.repository;

import com.example.playground.quote.domain.QuoteTrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteToTradeRepository extends JpaRepository<QuoteTrade, Long> {
}
