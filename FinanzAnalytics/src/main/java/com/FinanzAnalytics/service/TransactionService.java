package com.FinanzAnalytics.service;


import com.FinanzAnalytics.exceptions.InsufficientBalanceException;
import com.FinanzAnalytics.models.Account;
import com.FinanzAnalytics.models.Transaction;
import com.FinanzAnalytics.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id " + id));
    }


    public Transaction createTransaction(Transaction transaction) throws InsufficientBalanceException {
        // ...

        // Verificar si la cuenta origen tiene suficiente saldo
        Account originAccount = accountService.getAccount(transaction.getOriginAccountId());
        BigDecimal amount = BigDecimal.valueOf(transaction.getAmount()); // Convert Double to BigDecimal
        if (originAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("La cuenta origen no tiene suficiente saldo");
        }

        // Actualizar saldo de la cuenta origen
        originAccount.setBalance(originAccount.getBalance().subtract(amount));
        accountService.updateAccount(originAccount.getId(), originAccount);

        // Actualizar saldo de la cuenta destino
        Account destinationAccount = accountService.getAccount(transaction.getDestinationAccountId());
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
        accountService.updateAccount(destinationAccount.getId(), destinationAccount);

        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction transaction) {
        Transaction existingTransaction = getTransaction(id);
        // Puedes descomentar y actualizar otros campos según sea necesario
        // existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setCategory(transaction.getCategory());
        return transactionRepository.save(existingTransaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
