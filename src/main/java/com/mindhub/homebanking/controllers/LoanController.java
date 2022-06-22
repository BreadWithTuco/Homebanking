package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/api")
@RestController
public class LoanController {

    @Autowired
    ClientService clientService;
    @Autowired
    LoanService loanService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    @GetMapping(path = "/loans")
    public List<LoanDTO> getListLoans (){
        return loanService.getListLoans();
    }

    @Transactional
    @PostMapping(path = "/loans")
    public ResponseEntity<Object> createLoan (Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){

        Client clientCurrent = clientService.getClientByEmail(authentication.getName());
        Loan loan = loanService.getIdLoan(loanApplicationDTO.getIdLoan());
        Account accountDestiny = accountService.getAccountByNumber(loanApplicationDTO.getAccountNumber());

        if (loanApplicationDTO.getAmount() == null || loanApplicationDTO.getPayment() == null || loanApplicationDTO.getAccountNumber() == null || loanApplicationDTO.getIdLoan() == null){
            return new ResponseEntity<>("Missing Data",HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayment() <= 0){
            return new ResponseEntity<>("Payment or Amount are not the correct",HttpStatus.FORBIDDEN);
        }

        if (loan == null){
            return new ResponseEntity<>("Loan doesn't exist",HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayment())){
            return new ResponseEntity<>("This payment is not the correct on the loan",HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("the requested amount expends the loan",HttpStatus.FORBIDDEN);
        }

        if (accountDestiny == null){
            return new ResponseEntity<>("Account destiny doesn't exist",HttpStatus.FORBIDDEN);
        }

        if (!clientCurrent.getAccount().contains(accountDestiny)){
            return new ResponseEntity<>("This account is not yours bitch",HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(clientCurrent, loan,(loanApplicationDTO.getAmount())*1.2, loanApplicationDTO.getPayment());
        loanService.saveClientLoan(clientLoan);

        Transaction transaction = new Transaction(accountDestiny, TransactionType.CREDIT, loanApplicationDTO.getAmount(), accountDestiny.getBalance() + loanApplicationDTO.getAmount(), "You required loan," + " " + loan.getName() + ". " + "loan approved", LocalDateTime.now());
        transactionService.saveTransaction(transaction);

        accountDestiny.setBalance(accountDestiny.getBalance() + loanApplicationDTO.getAmount());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}