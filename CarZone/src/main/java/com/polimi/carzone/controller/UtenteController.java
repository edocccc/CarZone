package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.LoginRequestDTO;
import com.polimi.carzone.dto.request.ModificaUtenteRequestDTO;
import com.polimi.carzone.dto.request.SignupRequestDTO;
import com.polimi.carzone.dto.response.*;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.persistence.service.UtenteService;
import com.polimi.carzone.security.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// annotazione che definisce la classe come controller
@RestController
// annotazione che definisce il path di base per le chiamate a questo controller
@RequestMapping("/api/v1/utente")
// annotazione che genera un costruttore con tutti i parametri
@RequiredArgsConstructor
public class UtenteController {

    // dichiarazione del service dell'utente
    private final UtenteService utenteService;

    // dichiarazione del service per la gestione dei token
    private final TokenUtil tokenUtil;


    // endpoint che permette ad un utente di accedere al sistema
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        // chiamata al metodo del service per effettuare il login
        Utente utente = utenteService.login(request);
        // generazione del token jwt per l'utente
        String token = tokenUtil.generaToken(utente);
        // creazione della risposta
        LoginResponseDTO response = new LoginResponseDTO();
        //settaggio dei campi della risposta
        response.setId(utente.getId());
        response.setEmail(utente.getEmail());
        response.setNome(utente.getNome());
        response.setCognome(utente.getCognome());
        response.setUsername(utente.getUsername());
        response.setDataNascita(utente.getDataNascita());
        response.setRuolo(utente.getRuolo());
        response.setToken(token);
        // ritorno un header con il token e il body con la risposta
        return ResponseEntity.status(HttpStatus.OK).header("Authorization", token).body(response);
    }

    // endpoint che permette di registrare un cliente
    @PostMapping("/signup")
    public ResponseEntity<RegistrazioneResponseDTO> registrazioneCliente(@RequestBody SignupRequestDTO request) {
        // chiamata al metodo del service per registrare un cliente
        utenteService.registrazioneCliente(request);
        // creazione della risposta tramite il DTO e settaggio del messaggio
        RegistrazioneResponseDTO response = new RegistrazioneResponseDTO("Registrazione effettuata con successo");
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che permette al manager di registrare un dipendente
    @PostMapping("/registraDipendente")
    public ResponseEntity<RegistrazioneResponseDTO> registrazioneDipendente(@RequestBody SignupRequestDTO request) {
        // chiamata al metodo del service per il manager per registrare un dipendente
        utenteService.registrazioneDipendente(request);
        // creazione della risposta tramite il DTO e settaggio del messaggio
        RegistrazioneResponseDTO response = new RegistrazioneResponseDTO("Registrazione effettuata con successo");
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che permette al manager di trovare tutti gli utenti
    @GetMapping("/utentiManager")
    public ResponseEntity<List<UtenteManagerResponseDTO>> trovaUtentiManager() {
        // chiamata al metodo del service per il manager per trovare tutti gli utenti
        // creazione della risposta tramite il DTO e i dati tornati dal serice
        List<UtenteManagerResponseDTO> utenti = utenteService.trovaUtentiManager();
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(utenti);
    }

    // endpoint che permette al manager di trovare un utente per la modifica in base all'id
    @GetMapping("/trova/{idUtente}")
    public ResponseEntity<UtenteManagerResponseDTO> trovaUtente(@PathVariable Long idUtente) {
        // chiamata al metodo del service per il manager per trovare un utente in base all'id
        // creazione della risposta tramite il DTO e i dati tornati dal serice
        UtenteManagerResponseDTO utente = utenteService.trovaUtenteManager(idUtente);
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(utente);
    }

    // endpoint che permette al manager di modificare un utente in base all'id
    @PutMapping("/modifica/{idUtente}")
    public ResponseEntity<ModificaUtenteResponseDTO> modificaUtente(@PathVariable Long idUtente, @RequestBody ModificaUtenteRequestDTO request) {
        // chiamata al metodo del service per il manager per modificare un utente in base all'id e alla richiesta
        utenteService.modificaUtente(idUtente, request);
        // creazione della risposta tramite il DTO e settaggio del messaggio
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(new ModificaUtenteResponseDTO("Utente modificato con successo"));
    }

    // endpoint che permette al manager di eliminare un utente in base all'id
    @DeleteMapping("/elimina/{idUtente}")
    public ResponseEntity<EliminaUtenteResponseDTO> eliminaUtente(@PathVariable Long idUtente) {
        // chiamata al metodo del service per il manager per eliminare un utente in base all'id
        utenteService.eliminaUtente(idUtente);
        // creazione della risposta tramite il DTO e settaggio del messaggio
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(new EliminaUtenteResponseDTO("Utente eliminato con successo"));
    }

    // endpoint che permette al manager di trovare tutti i clienti
    @GetMapping("/trovaClienti")
    public ResponseEntity<List<UtenteManagerResponseDTO>> trovaClienti() {
        // chiamata al metodo del service per trovare tutti i clienti
        // creazione della risposta tramite il DTO e i dati tornati dal serice
        List<UtenteManagerResponseDTO> utenti = utenteService.trovaClienti();
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(utenti);
    }

    // endpoint che permette al manager di trovare tutti i dipendenti
    @GetMapping("/trovaDipendenti")
    public ResponseEntity<List<UtenteManagerResponseDTO>> trovaDipendenti() {
        // chiamata al metodo del service per trovare tutti i dipendenti
        // creazione della risposta tramite il DTO e i dati tornati dal serice
        List<UtenteManagerResponseDTO> utenti = utenteService.trovaDipendenti();
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(utenti);
    }
}

