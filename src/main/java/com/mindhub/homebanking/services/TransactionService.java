package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface TransactionService {

    Set<TransactionDTO> getTransactionsDTO(Authentication authentication, Long id);
    void saveTransaction(Transaction transaction);

}
