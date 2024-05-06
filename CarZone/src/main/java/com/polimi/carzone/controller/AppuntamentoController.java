package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.PrenotazioneRequestDTO;
import com.polimi.carzone.dto.request.PresaInCaricoRequestDTO;
import com.polimi.carzone.dto.response.AppuntamentoResponseDTO;
import com.polimi.carzone.dto.response.PrenotazioneResponseDTO;
import com.polimi.carzone.dto.response.PresaInCaricoResponseDTO;
import com.polimi.carzone.dto.response.ValutazioneMediaResponseDTO;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appuntamento")
@RequiredArgsConstructor
public class AppuntamentoController {

    private final AppuntamentoService appuntamentoService;

    @PostMapping("/prenota")
    public ResponseEntity<PrenotazioneResponseDTO> prenota(@RequestBody PrenotazioneRequestDTO request){
        appuntamentoService.prenota(request);
        PrenotazioneResponseDTO response = new PrenotazioneResponseDTO("Prenotazione effettuata con successo!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/dipendente/{idDipendente}")
    public ResponseEntity<List<AppuntamentoResponseDTO>> trovaAppuntamentiDipendente(@PathVariable long idDipendente){
        List<AppuntamentoResponseDTO> response = appuntamentoService.trovaAppuntamentiDipendente(idDipendente);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/dipendente/{idDipendente}/valutazione")
    public ResponseEntity<ValutazioneMediaResponseDTO> calcolaValutazioneMediaDipendente(@PathVariable long idDipendente){
        ValutazioneMediaResponseDTO response = appuntamentoService.calcolaValutazioneMediaDipendente(idDipendente);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/appuntamentiLiberi")
    public ResponseEntity<List<AppuntamentoResponseDTO>> trovaAppuntamentiLiberi(){
        List<AppuntamentoResponseDTO> response = appuntamentoService.trovaAppuntamentiLiberi();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/prendiInCarico")
    public ResponseEntity<PresaInCaricoResponseDTO> prendiInCarico(@RequestBody PresaInCaricoRequestDTO request){
        appuntamentoService.prendiInCarico(request);
        PresaInCaricoResponseDTO response = new PresaInCaricoResponseDTO();
        response.setMessage("Appuntamento preso in carico con successo");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
