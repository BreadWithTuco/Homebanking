package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utils.getCardNumber;
import static com.mindhub.homebanking.utils.Utils.getRandomNumber;


@RequestMapping("/api")
@RestController
public class CardController {

    @Autowired
    CardService cardService;
    @Autowired
    ClientService clientService;

    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getCards(Authentication authentication){
        return cardService.getCards(authentication);
    }

    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> register(
            Authentication authentication,
            @RequestParam CardType cardType,
            @RequestParam CardColor cardColor
    ) {

        Client clientCurrent = clientService.getClientByEmail(authentication.getName()); //autenticado
        Set<Card> debitCards = clientCurrent.getCards().stream().filter(card -> card.getType().equals(CardType.DEBIT)).collect(Collectors.toSet());
        Set<Card> creditCards = clientCurrent.getCards().stream().filter(card -> card.getType().equals(CardType.CREDIT)).collect(Collectors.toSet());


        if (cardType.equals(cardType.CREDIT) && debitCards.size() >= 3){
            return new ResponseEntity<>("You cannot create more cards", HttpStatus.FORBIDDEN);
        }

        if (cardType.equals(cardType.DEBIT) && creditCards.size() >= 3){
            return new ResponseEntity<>("You cannot create more cards", HttpStatus.FORBIDDEN);
        }

        Card card = new Card(clientCurrent, cardType, cardColor, getCardNumber, String.valueOf(getRandomNumber(100, 999)), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
        cardService.saveCard(card);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping("/clients/current/cards")
    public ResponseEntity<?> deleteCard(Authentication authentication, @RequestParam Long id){
        Client clientCurrent = clientService.getClientByEmail(authentication.getName()); //autenticado

        if (id == null){
            return new ResponseEntity<>("Missing Data",HttpStatus.FORBIDDEN);
        }

        Card cardToDelete = cardService.getCardById(id);

        if (cardToDelete == null){
            return new ResponseEntity<>("Card doesn't exist",HttpStatus.FORBIDDEN);
        }

        if (!clientCurrent.getCards().contains(cardToDelete)){
            return new ResponseEntity<>("This card is not yours",HttpStatus.FORBIDDEN);
        }

        if (!cardToDelete.isEnabled()){
            return new ResponseEntity<>("Is already deleted",HttpStatus.FORBIDDEN);
        }

        cardService.deleteCard(cardToDelete);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
