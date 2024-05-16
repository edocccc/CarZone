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

@RestController
@RequestMapping("/api/v1/utente")
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService utenteService;

    private final TokenUtil tokenUtil;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        Utente utente = utenteService.login(request);
        String token = tokenUtil.generaToken(utente);
        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(utente.getId());
        response.setEmail(utente.getEmail());
        response.setNome(utente.getNome());
        response.setCognome(utente.getCognome());
        response.setUsername(utente.getUsername());
        response.setDataNascita(utente.getDataNascita());
        response.setRuolo(utente.getRuolo());
        response.setToken(token);
        return ResponseEntity.status(HttpStatus.OK).header("Authorization", token).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<RegistrazioneResponseDTO> registrazioneCliente(@RequestBody SignupRequestDTO request) {
        utenteService.registrazioneCliente(request);
        RegistrazioneResponseDTO response = new RegistrazioneResponseDTO("Registrazione effettuata con successo");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/registraDipendente")
    public ResponseEntity<RegistrazioneResponseDTO> registrazioneDipendente(@RequestBody SignupRequestDTO request) {
        utenteService.registrazioneDipendente(request);
        RegistrazioneResponseDTO response = new RegistrazioneResponseDTO("Registrazione effettuata con successo");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/utentiManager")
    public ResponseEntity<List<UtenteManagerResponseDTO>> trovaUtentiManager() {
        List<UtenteManagerResponseDTO> utenti = utenteService.trovaUtentiManager();
        return ResponseEntity.status(HttpStatus.OK).body(utenti);
    }

    @GetMapping("/trova/{idUtente}")
    public ResponseEntity<UtenteManagerResponseDTO> trovaUtente(@PathVariable Long idUtente) {
        UtenteManagerResponseDTO utente = utenteService.trovaUtenteManager(idUtente);
        return ResponseEntity.status(HttpStatus.OK).body(utente);
    }

    @PutMapping("/modifica/{idUtente}")
    public ResponseEntity<ModificaUtenteResponseDTO> modificaUtente(@PathVariable Long idUtente, @RequestBody ModificaUtenteRequestDTO request) {
        utenteService.modificaUtente(idUtente, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ModificaUtenteResponseDTO("Utente modificato con successo"));
    }

    @DeleteMapping("/elimina/{idUtente}")
    public ResponseEntity<EliminaUtenteResponseDTO> eliminaUtente(@PathVariable Long idUtente) {
        utenteService.eliminaUtente(idUtente);
        return ResponseEntity.status(HttpStatus.OK).body(new EliminaUtenteResponseDTO("Utente eliminato con successo"));
    }

    @GetMapping("/trovaClienti")
    public ResponseEntity<List<UtenteManagerResponseDTO>> trovaClienti() {
        List<UtenteManagerResponseDTO> utenti = utenteService.trovaClienti();
        return ResponseEntity.status(HttpStatus.OK).body(utenti);
    }

    @GetMapping("/trovaDipendenti")
    public ResponseEntity<List<UtenteManagerResponseDTO>> trovaDipendenti() {
        List<UtenteManagerResponseDTO> utenti = utenteService.trovaDipendenti();
        return ResponseEntity.status(HttpStatus.OK).body(utenti);
    }
}

