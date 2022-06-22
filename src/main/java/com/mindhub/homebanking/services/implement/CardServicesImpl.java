package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServicesImpl implements CardService {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientService clientService;

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card getCardById(long id) {
        return cardRepository.getById(id);
    }

    @Override
    public void deleteCard(Card card) {
        card.setEnabled(false);
        this.saveCard(card);
    }

    @Override
    public Set<CardDTO> getCards(Authentication authentication) {
        Client currentClient = clientService.getClientCurrent(authentication);
        return currentClient.getCards().stream().filter(card -> card.isEnabled() == true).map(CardDTO::new).collect(Collectors.toSet());
    }


}
