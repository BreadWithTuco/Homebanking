package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;

@RequestMapping("/api")
@RestController
public class TransactionController {


    @Autowired
    TransactionService transactionService;

    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;

    @GetMapping("/clients/current/transactions")
    public ResponseEntity<?> getTransactions(Authentication authentication,@RequestParam Long id) {
        Client clientCurrent = clientService.getClientCurrent(authentication);
        Account account = accountService.getAccountById(id);
        Set<Transaction> transactions = accountService.getAccountById(id).getTransactions();
        if (id == null){
            return new ResponseEntity<>("Missing Data",HttpStatus.FORBIDDEN);
        }

        if (account == null){
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (!clientCurrent.getAccount().contains(account)){
            return new ResponseEntity<>("This transactions can't found in this account",HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(transactionService.getTransactionsDTO(authentication,id),HttpStatus.ACCEPTED);
    }

    @Transactional
    @PostMapping(path = "/clients/current/transactions")
    public ResponseEntity<Object> createTransaction(
            Authentication authentication, @RequestParam double amount,
            @RequestParam String description, @RequestParam String accountOrigenNumber,
            @RequestParam String accountDestinyNumber
            ){

        Client clientCurrent = clientService.getClientByEmail(authentication.getName()); //cliente autenticado buscado por el email
        Account accountOrigen = accountService.getAccountByNumber(accountOrigenNumber);
        Account accountDestiny = accountService.getAccountByNumber(accountDestinyNumber);


        if (description.isEmpty() || accountOrigenNumber.isEmpty() || accountDestinyNumber.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (amount <= 0){
            return new ResponseEntity<>("You can't transfer 0 or lower amount",HttpStatus.FORBIDDEN);
        }

        if (accountOrigenNumber == accountDestinyNumber){
            return new ResponseEntity<>("Accounts are the same", HttpStatus.FORBIDDEN);
        }

        if (accountOrigen ==  null){
            return new ResponseEntity<>("Origin account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (clientCurrent.getAccount().contains(accountOrigenNumber)){
            return new ResponseEntity<>("This is not ur account madafaka", HttpStatus.FORBIDDEN);
        }

        if (accountDestiny ==  null){
            return new ResponseEntity<>("Destination account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (accountOrigen.getBalance() < amount){
            return new ResponseEntity<>("You not have moni", HttpStatus.FORBIDDEN);
        }

        transactionService.saveTransaction(new Transaction(accountOrigen, TransactionType.DEBIT, amount,accountOrigen.getBalance() - amount, "Transfer to " + accountDestinyNumber + " - " + description, LocalDateTime.now()));
        transactionService.saveTransaction(new Transaction(accountDestiny, TransactionType.CREDIT, amount,accountDestiny.getBalance() + amount,  "Receive from " + accountOrigenNumber + " - " + description, LocalDateTime.now()));

        accountOrigen.setBalance(accountOrigen.getBalance() - amount);
        accountDestiny.setBalance(accountDestiny.getBalance() + amount);
        accountService.saveAccount(accountOrigen);
        accountService.saveAccount(accountDestiny);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
