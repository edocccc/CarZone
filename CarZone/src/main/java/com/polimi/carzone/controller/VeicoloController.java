package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.response.VeicoloResponseDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
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
        if (esito) {
            return ResponseEntity.status(HttpStatus.OK).body("Aggiunta effettuata con successo!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Aggiunta fallita!");
        }
    }

    @GetMapping("/veicoli")
    public ResponseEntity<List<VeicoloResponseDTO>> stampaVeicoli() {
        List<VeicoloResponseDTO> veicoli = veicoloService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(veicoli);
    }
}
