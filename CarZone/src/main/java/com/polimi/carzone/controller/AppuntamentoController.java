package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.*;
import com.polimi.carzone.dto.response.*;
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

    @GetMapping("/recensioni/{idDipendente}")
    public ResponseEntity<List<RecensioneResponseDTO>> trovaRecensioniDipendente(@PathVariable long idDipendente){
        List<RecensioneResponseDTO> response = appuntamentoService.trovaRecensioniDipendente(idDipendente);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/dipendentiConRecensioni")
    public ResponseEntity<List<DipendenteConRecensioneDTO>> trovaDipendentiConRecensioni(){
        List<DipendenteConRecensioneDTO> response = appuntamentoService.trovaDipendentiConRecensioni();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/trovaPerManager")
    public ResponseEntity<List<AppuntamentoManagerResponseDTO>> trovaAppuntamentiPerManager(){
        List<AppuntamentoManagerResponseDTO> response = appuntamentoService.trovaAppuntamentiPerManager();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/prenotaPerManager")
    public ResponseEntity<PrenotazioneResponseDTO> prenotaPerManager(@RequestBody PrenotazioneManagerRequestDTO request){
        appuntamentoService.prenotaPerManager(request);
        PrenotazioneResponseDTO response = new PrenotazioneResponseDTO("Prenotazione effettuata con successo!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/elimina/{idAppuntamento}")
    public ResponseEntity<EliminaUtenteResponseDTO> eliminaUtente(@PathVariable long idAppuntamento) {
        appuntamentoService.eliminaAppuntamento(idAppuntamento);
        return ResponseEntity.status(HttpStatus.OK).body(new EliminaUtenteResponseDTO("Appuntamento eliminato con successo"));
    }

    @PutMapping("/modifica/{idAppuntamento}")
    public ResponseEntity<ModificaAppuntamentoResponseDTO> modificaAppuntamento(@PathVariable long idAppuntamento, @RequestBody ModificaAppuntamentoRequestDTO request) {
        appuntamentoService.modificaAppuntamento(idAppuntamento, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ModificaAppuntamentoResponseDTO("Appuntamento modificato con successo"));
    }

    @GetMapping("/appuntamentiCliente/{idCliente}")
    public ResponseEntity<List<AppuntamentoConRecensioneResponseDTO>> trovaAppuntamentiCliente(@PathVariable long idCliente){
        List<AppuntamentoConRecensioneResponseDTO> response = appuntamentoService.trovaAppuntamentiCliente(idCliente);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/lasciaRecensione")
    public ResponseEntity<LasciaRecensioneResponseDTO> lasciaRecensione(@RequestBody LasciaRecensioneRequestDTO request){
        appuntamentoService.lasciaRecensione(request);
        LasciaRecensioneResponseDTO response = new LasciaRecensioneResponseDTO("Recensione lasciata con successo!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/recensioniCliente/{idCliente}")
    public ResponseEntity<List<RecensioneClienteResponseDTO>> trovaRecensioniCliente(@PathVariable long idCliente){
        List<RecensioneClienteResponseDTO> response = appuntamentoService.trovaRecensioniCliente(idCliente);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
