package com.FinanzAnalytics.service;

import com.FinanzAnalytics.models.Account;
import com.FinanzAnalytics.models.User;
import com.FinanzAnalytics.repositories.AccountRepository;
import com.FinanzAnalytics.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account createAccount(Account account) {
        // Verificar si el usuario existe antes de asignar
        User user = userRepository.findById(account.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        account.setUser(user);
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account account) {
        Account existingAccount = getAccount(id);
        existingAccount.setBalance(account.getBalance());
        existingAccount.setAccountNumber(account.getAccountNumber()); // Asegúrate de actualizar otros campos necesarios
        existingAccount.setAccountType(account.getAccountType());
        // Puedes actualizar el usuario si es necesario
        if (account.getUser() != null) {
            User user = userRepository.findById(account.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingAccount.setUser(user);
        }
        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
