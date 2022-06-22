package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

import static com.mindhub.homebanking.utils.Utils.getRandomNumber;

@RequestMapping("/api")
@RestController
public class AccountController {


    @Autowired
    AccountService accountService;
    @Autowired
    ClientService clientService;


    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccount(Authentication authentication) {
        return accountService.getAccount(authentication);
    }
    @GetMapping("/clients/current/accounts/{id}")
    public AccountDTO getAccountDTO(@PathVariable Long id){
        return accountService.getAccountDTO(id);
    }
    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication){

        Client clientCurrent = clientService.getClientByEmail(authentication.getName()); //autenticado

        if (clientCurrent.getAccount().size() >= 3){
            return new ResponseEntity<>("You cannot create more accounts", HttpStatus.FORBIDDEN);
        }


        accountService.saveAccount(new Account(clientCurrent, "VIN-" + getRandomNumber(10000000, 99999999), LocalDateTime.now(), 0));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping("/clients/current/accounts")
    public ResponseEntity<?> deleteAccount(Authentication authentication, @RequestParam Long id){
        Client clientCurrent = clientService.getClientByEmail(authentication.getName()); //autenticado

        if (id == null){
            return new ResponseEntity<>("Missing Data",HttpStatus.FORBIDDEN);
        }

        Account accountToDelete = accountService.getAccountById(id);

        if (accountToDelete == null){
            return new ResponseEntity<>("Account Doesn't exist",HttpStatus.FORBIDDEN);
        }

        if (!clientCurrent.getAccount().contains(accountToDelete)){
            return new ResponseEntity<>("This account is not yours",HttpStatus.FORBIDDEN);
        }

        if (!accountToDelete.isEnabled()){
            return new ResponseEntity<>("Is already deleted",HttpStatus.FORBIDDEN);
        }

//        if(accountToDelete.getTransactions().contains())

        accountService.deleteAccount(accountToDelete);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}