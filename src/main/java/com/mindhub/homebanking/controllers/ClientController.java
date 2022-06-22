package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.utils.Utils.getRandomNumber;

@RequestMapping("/api")
@RestController
public class ClientController {


    @Autowired  //NO SE PORQUE FUNCIONA, PERO NO BORRAR (LO SAQUE Y NO ME DEJA ENTRAR AL /API/CLIENTS) ADEMAS MI LOGICA FUE LA DE QUE LLAMAMOS AL REPOSITORIO NO HACE FALTA EL BEAN...
    ClientService clientService;

    @Autowired
    AccountService accountService;


    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClientsDTO();
    }
    @GetMapping("clients/{id}")
    public ClientDTO getClientDTO(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }
    @GetMapping("clients/current")
    public ClientDTO getCurrent(Authentication authentication) {
        return new ClientDTO(clientService.getClientCurrent(authentication));
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path = "/clients")

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {


        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }


        if (clientService.getClientByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }


        Client current = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(current);


        accountService.saveAccount(new Account(current, "VIN-" + getRandomNumber(10000000, 99999999), LocalDateTime.now(), 0));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
