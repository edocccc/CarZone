package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.DettagliVeicoloRequestDTO;
import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloResponseDTO;
import com.polimi.carzone.dto.response.VeicoloResponseDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import com.polimi.carzone.strategy.implementation.RicercaTarga;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/veicolo")
@RequiredArgsConstructor
public class VeicoloController {

    private final VeicoloService veicoloService;

    @PostMapping("/aggiungiVeicolo")
    public ResponseEntity<String> aggiungiVeicolo(@RequestBody AggiuntaVeicoloRequestDTO request) {
        boolean esito = veicoloService.aggiungiVeicolo(request);
        return ResponseEntity.status(HttpStatus.OK).body("Aggiunta effettuata con successo!");
    }

    @GetMapping("/veicoli")
    public ResponseEntity<List<VeicoloResponseDTO>> stampaVeicoli() {
        List<VeicoloResponseDTO> veicoli = veicoloService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(veicoli);
    }

    @GetMapping("/dettagli/{idVeicolo}")
    public ResponseEntity<DettagliVeicoloResponseDTO> mostraDettagli(@PathVariable String idVeicolo) {
        long id = Long.parseLong(idVeicolo);
        DettagliVeicoloResponseDTO dettagli= veicoloService.recuperaDettagli(id);
        return ResponseEntity.status(HttpStatus.OK).body(dettagli);
    }

    @PostMapping("/cerca")
    public ResponseEntity<List<VeicoloResponseDTO>> cercaVeicoli(@RequestBody RicercaRequestDTO request) {
        List<Veicolo> veicoliTrovati;
        List<VeicoloResponseDTO> veicoliResponse;
        RicercaStrategy ricercaStrategy = veicoloService.scegliRicerca(request.getCriterio());
        veicoliTrovati = ricercaStrategy.ricerca(request);
        veicoliResponse = veicoloService.convertiVeicoliInVeicoliResponse(veicoliTrovati);
        return ResponseEntity.status(HttpStatus.OK).body(veicoliResponse);
    }
}
