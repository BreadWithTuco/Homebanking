package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface AccountService {

    Set<AccountDTO> getAccount(Authentication authentication);
    AccountDTO getAccountDTO(long id);
    Account getAccountByNumber(String number);
    Account getAccountById(Long id);
    void saveAccount(Account account);
    void deleteAccount(Account account);
}
