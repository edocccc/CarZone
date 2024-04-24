package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.PrenotazioneRequestDTO;
import com.polimi.carzone.dto.response.PrenotazioneResponseDTO;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appuntamento")
@RequiredArgsConstructor
public class AppuntamentoController {

    private final AppuntamentoService appuntamentoService;

    @PostMapping("/prenota")
    public ResponseEntity<PrenotazioneResponseDTO> prenota(@RequestBody PrenotazioneRequestDTO request){
        appuntamentoService.prenota(request);
        PrenotazioneResponseDTO response = new PrenotazioneResponseDTO("Prenotazione effettuata con successo!");
        return ResponseEntity.ok(response);
    }
}
