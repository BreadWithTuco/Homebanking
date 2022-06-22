package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123"));
			Client client2 = new Client("Augusto", "Casanova", "casanovaugusto01@gmail.com", passwordEncoder.encode("123"));
			Client admin = new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("admin"));
			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(admin);


			Account account1 = new Account(client1, "VIN001", LocalDateTime.now(), 5000.0);
			Account account2 = new Account(client1, "VIN002", LocalDateTime.now().plusDays(1), 7500.0);
			Account account3 = new Account(client2, "VIN003", LocalDateTime.now(), 5000.0);
			Account account4 = new Account(client2, "VIN004", LocalDateTime.now().plusDays(1), 7500.0);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);


			Transaction transaction1 = new Transaction(account1, TransactionType.CREDIT, 15000.99,account1.getBalance() + 15000.99, "Taxes",LocalDateTime.now());
			Transaction transaction2 = new Transaction(account1, TransactionType.DEBIT, 30000.50,account1.getBalance() - 30000.50, "Monitor",LocalDateTime.now().plusDays(1));
			Transaction transaction3 = new Transaction(account1, TransactionType.CREDIT, 5000.23,account1.getBalance() + 5000.23, "Mall",LocalDateTime.now());
			Transaction transaction4 = new Transaction(account1, TransactionType.DEBIT, 50000.30,account1.getBalance() - 50000.30, "Gaming",LocalDateTime.now().plusDays(1));
			Transaction transaction5 = new Transaction(account2, TransactionType.CREDIT, 2000.40,account2.getBalance() + 2000.40, "Pharmacy",LocalDateTime.now());
			Transaction transaction6 = new Transaction(account2, TransactionType.DEBIT, 1000.55,account2.getBalance() - 1000.55, "Food",LocalDateTime.now().plusDays(1));
			Transaction transaction7 = new Transaction(account2, TransactionType.CREDIT, 450.50,account2.getBalance() + 450.50, "IPA",LocalDateTime.now());
			Transaction transaction8 = new Transaction(account2, TransactionType.DEBIT, 500.0,account2.getBalance() - 500.0, "Chocolate",LocalDateTime.now().plusDays(1));
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);


			Loan loanMortgage = new Loan("Mortgage", 500000, List.of(12,24,36,48,60));
			Loan loanCar = new Loan("Car", 100000, List.of(6,12,24));
			Loan loanPersonal = new Loan("Personal", 300000, List.of(6,12,24,36));
			loanRepository.save(loanMortgage);
			loanRepository.save(loanCar);
			loanRepository.save(loanPersonal);


			ClientLoan clientLoanHipotecario1 = new ClientLoan(client1, loanMortgage, 400000, 60);
			ClientLoan clientLoanPersonal1 = new ClientLoan(client1, loanPersonal, 50000, 12);
			ClientLoan clientLoanPersonal2 = new ClientLoan(client2, loanPersonal, 100000, 24);
			ClientLoan clientLoanAutomotriz1 = new ClientLoan(client2, loanCar, 200000, 36);
			clientLoanRepository.save(clientLoanHipotecario1);
			clientLoanRepository.save(clientLoanPersonal1);
			clientLoanRepository.save(clientLoanPersonal2);
			clientLoanRepository.save(clientLoanAutomotriz1);


			Card card1 = new Card(client1, CardType.DEBIT, CardColor.GOLD, "3342-5519-4378-9870", "323", LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card2 = new Card(client1, CardType.CREDIT, CardColor.TITANIUM, "5678-2789-4466-1789", "037", LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card3 = new Card(client1, CardType.CREDIT, CardColor.SILVER, "9478-0427-7534-9156", "643", LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card4 = new Card(client1, CardType.DEBIT, CardColor.SILVER, "6823-9045-8293-8953", "152", LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card5 = new Card(client1, CardType.CREDIT, CardColor.GOLD, "9328-5398-4109-0923", "987", LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card6 = new Card(client1, CardType.DEBIT, CardColor.TITANIUM, "5289-6403-9628-7433", "428", LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card7 = new Card(client2, CardType.CREDIT, CardColor.SILVER, "5902-0954-9970-2161", "890", LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card8 = new Card(client2, CardType.CREDIT, CardColor.SILVER, "5389-1240-0285-4287", "623", LocalDateTime.now().minusYears(5), LocalDateTime.now());
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);
			cardRepository.save(card5);
			cardRepository.save(card6);
			cardRepository.save(card7);
			cardRepository.save(card8);


		};
	}
}