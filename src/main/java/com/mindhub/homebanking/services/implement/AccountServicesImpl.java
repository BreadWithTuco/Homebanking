package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.AccountRepository;
import com.mindhub.homebanking.repository.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServicesImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientService clientService;

    @Override
    public Set<AccountDTO> getAccount(Authentication authentication) {
        Client clientCurrent = clientService.getClientCurrent(authentication);
        Set<Account> accounts = clientCurrent.getAccount();
        return accounts.stream().filter(account -> account.isEnabled() == true).map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public AccountDTO getAccountDTO(long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account getAccountById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAccount(Account account){
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Account account) {
        account.setEnabled(false);
        this.saveAccount(account);
        account.getTransactions().forEach(transaction -> {
            transaction.setEnabled(false);
            transactionRepository.save(transaction);
        });
        this.saveAccount(account);
    }
}
