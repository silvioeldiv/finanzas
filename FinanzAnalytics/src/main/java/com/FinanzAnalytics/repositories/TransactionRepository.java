package com.FinanzAnalytics.repositories;

import com.FinanzAnalytics.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}