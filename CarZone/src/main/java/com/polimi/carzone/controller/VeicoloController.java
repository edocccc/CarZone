package com.polimi.carzone.controller;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.ModificaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.RegistrazioneVenditaRequestDTO;
import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.dto.response.*;
import com.polimi.carzone.model.Appuntamento;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.repository.AppuntamentoRepository;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import com.polimi.carzone.persistence.service.UtenteService;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import com.polimi.carzone.strategy.context.RicercaManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/veicolo")
@RequiredArgsConstructor
public class VeicoloController {

    private final VeicoloService veicoloService;
    private final AppuntamentoService appuntamentoService;
    private final UtenteService utenteService;
    private final RicercaManager ricercaManager;
    private final AppuntamentoRepository appuntamentoRepo;

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
    public ResponseEntity<DettagliVeicoloManagerResponseDTO> mostraDettagli(@PathVariable String idVeicolo) {
        long id = Long.parseLong(idVeicolo);
        DettagliVeicoloManagerResponseDTO dettagli= veicoloService.recuperaDettagli(id);
        return ResponseEntity.status(HttpStatus.OK).body(dettagli);
    }

    @PostMapping("/cerca")
    public ResponseEntity<List<VeicoloResponseDTO>> cercaVeicoli(@RequestBody RicercaRequestDTO request) {
        List<Veicolo> veicoliTrovati;
        List<VeicoloResponseDTO> veicoliResponse;
        RicercaStrategy ricercaStrategy = ricercaManager.scegliRicerca(request.getCriterio());
        veicoliTrovati = ricercaManager.executeRicerca(ricercaStrategy,request);
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
            appuntamentoService.registraVendita(request.getIdAppuntamento(), true);
            response.setMessaggio("Registrazione esito positivo effettuata con successo");

        } else {
            appuntamentoService.registraVendita(request.getIdAppuntamento(), false);
            response.setMessaggio("Registrazione esito negativo effettuata con successo");
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
    public ResponseEntity<ModificaVeicoloResponseDTO> modificaVeicolo(@PathVariable long idVeicolo, @RequestBody ModificaVeicoloRequestDTO request) {
        veicoloService.modificaVeicolo(idVeicolo, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ModificaVeicoloResponseDTO("Veicolo modificato con successo"));
    }

    @GetMapping("/veicoliDisponibili")
    public ResponseEntity<List<DettagliVeicoloManagerResponseDTO>> stampaVeicoliDiponibiliPerManager() {
        List<DettagliVeicoloManagerResponseDTO> veicoliDisponobili = veicoloService.findAllDisponibili();
        return ResponseEntity.status(HttpStatus.OK).body(veicoliDisponobili);
    }

    @GetMapping("/prova/{idVeicolo}")
    public ResponseEntity<Optional<List<Appuntamento>>> prova(@PathVariable long idVeicolo) {
        Optional<List<Appuntamento>> prova=appuntamentoRepo.findByVeicolo_IdAndEsitoRegistratoIsFalse(idVeicolo);
        return ResponseEntity.status(HttpStatus.OK).body(prova);
    }
}
