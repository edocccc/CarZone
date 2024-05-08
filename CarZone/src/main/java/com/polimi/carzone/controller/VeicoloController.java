package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.ModificaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.RegistrazioneVenditaRequestDTO;
import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.dto.response.*;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import com.polimi.carzone.persistence.service.UtenteService;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
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
    private final AppuntamentoService appuntamentoService;
    private final UtenteService utenteService;

    @PostMapping("/aggiungiVeicolo")
    public ResponseEntity<AggiuntaVeicoloResponseDTO> aggiungiVeicolo(@RequestBody AggiuntaVeicoloRequestDTO request) {
        veicoloService.aggiungiVeicolo(request);
        return ResponseEntity.status(HttpStatus.OK).body(new AggiuntaVeicoloResponseDTO("Veicolo aggiunto con successo"));
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

    @PostMapping("/registraVendita")
    public ResponseEntity<RegistrazioneVenditaResponseDTO> registraVendita(@RequestBody RegistrazioneVenditaRequestDTO request) {
        RegistrazioneVenditaResponseDTO response = new RegistrazioneVenditaResponseDTO();
        if(request.isVenditaConclusa()){
            long idVeicolo = appuntamentoService.trovaIdVeicolo(request.getIdAppuntamento());
            long idCliente = appuntamentoService.trovaIdCliente(request.getIdAppuntamento());
            Utente acquirente = utenteService.findById(idCliente);
            veicoloService.registraVendita(idVeicolo, acquirente);
            response.setMessaggio("Vendita effettuata con successo");

        } else {
            response.setMessaggio("Vendita non effettuata");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/veicoliConDettagli")
    public ResponseEntity<List<DettagliVeicoloManagerResponseDTO>> stampaVeicoliConDettagli() {
        List<DettagliVeicoloManagerResponseDTO> veicoliConDettagli = veicoloService.findAllConDettagli();
        return ResponseEntity.status(HttpStatus.OK).body(veicoliConDettagli);
    }

    @DeleteMapping("/elimina/{idVeicolo}")
    public ResponseEntity<EliminaVeicoloResponseDTO> eliminaVeicolo(@PathVariable long idVeicolo) {
        veicoloService.eliminaVeicolo(idVeicolo);
        return ResponseEntity.status(HttpStatus.OK).body(new EliminaVeicoloResponseDTO("Veicolo eliminato con successo"));
    }

    @PutMapping("/modifica/{idVeicolo}")
    public ResponseEntity<ModificaVeicoloResponse> modificaVeicolo(@PathVariable long idVeicolo, @RequestBody ModificaVeicoloRequestDTO request) {
        veicoloService.modificaVeicolo(idVeicolo, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ModificaVeicoloResponse("Veicolo modificato con successo"));
    }
}
