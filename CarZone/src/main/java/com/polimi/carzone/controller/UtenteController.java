package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.LoginRequestDTO;
import com.polimi.carzone.dto.request.SignupRequestDTO;
import com.polimi.carzone.dto.response.LoginResponseDTO;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.persistence.service.UtenteService;
import com.polimi.carzone.security.TokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
        response.setUsername(utente.getUsername());
        return ResponseEntity.status(HttpStatus.OK).header("Authorization", token).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registrazioneCliente(@Valid @RequestBody SignupRequestDTO request) {
        boolean esito = utenteService.registrazioneCliente(request);
        if (esito) {
            return ResponseEntity.status(HttpStatus.OK).body("Registrazione effettuata con successo!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registrazione fallita!");
        }
    }

    @PostMapping("/manager/registraDipendente")
    public ResponseEntity<String> registrazioneDipendente(@Valid @RequestBody SignupRequestDTO request) {
        boolean esito = utenteService.registrazioneDipendente(request);
        if (esito) {
            return ResponseEntity.status(HttpStatus.OK).body("Registrazione effettuata con successo!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registrazione fallita!");
        }
    }

    @GetMapping("/manager")
    public ResponseEntity<String> manager(UsernamePasswordAuthenticationToken auth) {
        Utente utente = (Utente) auth.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(utente.getUsername() + " dice: Lazio Merda!");
    }
}

