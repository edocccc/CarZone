package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.*;
import com.polimi.carzone.dto.response.*;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

// annotazione per la definizione di un controller
@RestController
// definizione del path principale per le chiamate a questo controller
@RequestMapping("/api/v1/appuntamento")
// annotazione per la creazione di un costruttore con tutti i parametri
@RequiredArgsConstructor
public class AppuntamentoController {

    // dichiarazione del service dell'appuntamento
    private final AppuntamentoService appuntamentoService;

    // endpoint per la creazione di un appuntamento
    @PostMapping("/prenota")
    public ResponseEntity<PrenotazioneResponseDTO> prenota(@RequestBody PrenotazioneRequestDTO request, @RequestHeader("Authorization") String token) throws IOException {
        //chiamata al metodo del service per la creazione di un appuntamento
        appuntamentoService.prenota(request, token);
        // creazione della risposta tramite il DTO
        PrenotazioneResponseDTO response = new PrenotazioneResponseDTO("Prenotazione effettuata con successo!");
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che ritorna tutti gli appuntamenti di un dipendente dato il suo id
    @GetMapping("/dipendente/{idDipendente}")
    public ResponseEntity<List<AppuntamentoResponseDTO>> trovaAppuntamentiDipendente(@PathVariable Long idDipendente, @RequestHeader("Authorization") String token){
        // chiamata al metodo del service per trovare gli appuntamenti di un dipendente dato il suo id e il token
        // la risposta viene creata con i dati tornati dal service e il DTO
        List<AppuntamentoResponseDTO> response = appuntamentoService.trovaAppuntamentiDipendente(idDipendente, token);
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che ritorna la valutazione media di un dipendente dato il suo id
    @GetMapping("/dipendente/{idDipendente}/valutazione")
    public ResponseEntity<ValutazioneMediaResponseDTO> calcolaValutazioneMediaDipendente(@PathVariable Long idDipendente, @RequestHeader("Authorization") String token){
        // chiamata al metodo del service per calcolare la valutazione media di un dipendente dato il suo id e il token
        // la risposta viene creata con i dati tornati dal service e il DTO
        ValutazioneMediaResponseDTO response = appuntamentoService.calcolaValutazioneMediaDipendente(idDipendente, token);
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che ritorna tutti gli appuntamenti liberi (senza dipendente associato)
    @GetMapping("/appuntamentiLiberi")
    public ResponseEntity<List<AppuntamentoResponseDTO>> trovaAppuntamentiLiberi(){
        // chiamata al metodo del service per trovare gli appuntamenti liberi
        // la risposta viene creata con i dati tornati dal service e il DTO
        List<AppuntamentoResponseDTO> response = appuntamentoService.trovaAppuntamentiLiberi();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che permette di prendere in carico un appuntamento da parte di un dipendente
    @PostMapping("/prendiInCarico")
    public ResponseEntity<PresaInCaricoResponseDTO> prendiInCarico(@RequestBody PresaInCaricoRequestDTO request, @RequestHeader("Authorization") String token){
        // chiamata al metodo del service per prendere in carico un appuntamento passando la richiesta e il token
        appuntamentoService.prendiInCarico(request, token);
        // creazione della risposta tramite il DTO
        PresaInCaricoResponseDTO response = new PresaInCaricoResponseDTO();
        // settaggio del messaggio di risposta
        response.setMessage("Appuntamento preso in carico con successo");
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che permette di trovare tutte le recensioni di un dipendente dato il suo id
    @GetMapping("/recensioni/{idDipendente}")
    public ResponseEntity<List<RecensioneResponseDTO>> trovaRecensioniDipendente(@PathVariable Long idDipendente, @RequestHeader("Authorization") String token){
        // chiamata al metodo del service per trovare le recensioni di un dipendente dato il suo id e il token
        // la risposta viene creata con i dati tornati dal service e il DTO
        List<RecensioneResponseDTO> response = appuntamentoService.trovaRecensioniDipendente(idDipendente, token);
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che trova per ogni dipendente la valutazione media e tutte le recensioni associate
    @GetMapping("/dipendentiConRecensioni")
    public ResponseEntity<List<DipendenteConRecensioneDTO>> trovaDipendentiConRecensioni(){
        // chiamata al metodo del service per trovare i dipendenti con le recensioni
        // la risposta viene creata con i dati tornati dal service e il DTO
        List<DipendenteConRecensioneDTO> response = appuntamentoService.trovaDipendentiConRecensioni();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che ritorna tutti gli appuntamenti con i relativi dati (usato dal manager)
    @GetMapping("/trovaPerManager")
    public ResponseEntity<List<AppuntamentoManagerResponseDTO>> trovaAppuntamentiPerManager(){
        // chiamata al metodo del service per il manager per trovare gli appuntamenti
        // la risposta viene creata con i dati tornati dal service e il DTO
        List<AppuntamentoManagerResponseDTO> response = appuntamentoService.trovaAppuntamentiPerManager();
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che permette al manager di prenotare un appuntamento per un cliente
    @PostMapping("/prenotaPerManager")
    public ResponseEntity<PrenotazioneResponseDTO> prenotaPerManager(@RequestBody PrenotazioneManagerRequestDTO request){
        // chiamata al metodo del service per il manager per prenotare un appuntamento per un cliente passando la richiesta
        appuntamentoService.prenotaPerManager(request);
        // creazione della risposta tramite il DTO e il messaggio settato
        PrenotazioneResponseDTO response = new PrenotazioneResponseDTO("Prenotazione effettuata con successo!");
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che permette al manager di eliminare un appuntamento dato il suo id
    @DeleteMapping("/elimina/{idAppuntamento}")
    public ResponseEntity<EliminaUtenteResponseDTO> eliminaUtente(@PathVariable Long idAppuntamento) {
        // chiamata al metodo del service per il manager per eliminare un appuntamento dato il suo id
        appuntamentoService.eliminaAppuntamento(idAppuntamento);
        // ritorno la risposta creata con il DTO e il messaggio settato
        return ResponseEntity.status(HttpStatus.OK).body(new EliminaUtenteResponseDTO("Appuntamento eliminato con successo"));
    }

    // endpoint che permette al manager di modificare un appuntamento dato il suo id
    @PutMapping("/modifica/{idAppuntamento}")
    public ResponseEntity<ModificaAppuntamentoResponseDTO> modificaAppuntamento(@PathVariable Long idAppuntamento, @RequestBody ModificaAppuntamentoRequestDTO request) {
        // chiamata al metodo del service per il manager per modificare un appuntamento dato il suo id e la richiesta
        appuntamentoService.modificaAppuntamento(idAppuntamento, request);
        // ritorno la risposta creata con il DTO e il messaggio settato
        return ResponseEntity.status(HttpStatus.OK).body(new ModificaAppuntamentoResponseDTO("Appuntamento modificato con successo"));
    }

    // endpoint che ritorna tutti gli appuntamenti di un cliente dato il suo id
    @GetMapping("/appuntamentiCliente/{idCliente}")
    public ResponseEntity<List<AppuntamentoConRecensioneResponseDTO>> trovaAppuntamentiCliente(@PathVariable Long idCliente, @RequestHeader("Authorization") String token){
        // chiamata al metodo del service per trovare gli appuntamenti di un cliente dato il suo id e il token
        List<AppuntamentoConRecensioneResponseDTO> response = appuntamentoService.trovaAppuntamentiCliente(idCliente, token);
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che permette al cliente di lasciare una recensione
    @PostMapping("/lasciaRecensione")
    public ResponseEntity<LasciaRecensioneResponseDTO> lasciaRecensione(@RequestBody LasciaRecensioneRequestDTO request, @RequestHeader("Authorization") String token){
        // chiamata al metodo del service per lasciare una recensione
        appuntamentoService.lasciaRecensione(request, token);
        // creazione della risposta tramite il DTO con il messaggio settato
        LasciaRecensioneResponseDTO response = new LasciaRecensioneResponseDTO("Recensione lasciata con successo!");
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint che ritorna tutte le recensioni lasciate da un cliente dato il suo id
    @GetMapping("/recensioniCliente/{idCliente}")
    public ResponseEntity<List<RecensioneClienteResponseDTO>> trovaRecensioniCliente(@PathVariable Long idCliente, @RequestHeader("Authorization") String token){
        // chiamata al metodo del service per trovare le recensioni di un cliente dato il suo id e il token
        // la risposta viene creata con i dati tornati dal service e il DTO
        List<RecensioneClienteResponseDTO> response = appuntamentoService.trovaRecensioniCliente(idCliente, token);
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint permette al manager di trovare un appuntamento per la modifica dato il suo id
    @GetMapping("/trovaPerModifica/{idAppuntamento}")
    public ResponseEntity<AppuntamentoModificaResponseDTO> trovaPerModifica(@PathVariable Long idAppuntamento, @RequestHeader("Authorization") String token){
        // chiamata al metodo del service per il manager per trovare un appuntamento per la modifica dato il suo id e il token
        // la risposta viene creata con i dati tornati dal service e il DTO
        AppuntamentoModificaResponseDTO response = appuntamentoService.trovaPerModifica(idAppuntamento, token);
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
