package com.FinanzAnalytics.repositories;

import com.FinanzAnalytics.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
