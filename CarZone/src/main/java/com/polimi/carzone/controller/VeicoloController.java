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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// annotazione per definire la classe come controller
@RestController
// annotazione per definire il path di base per le chiamate a questo controller
@RequestMapping("/api/v1/veicolo")
// annotazione per generare un costruttore con tutti i parametri
@RequiredArgsConstructor
public class VeicoloController {

    // dichiarazione del service dei veicoli
    private final VeicoloService veicoloService;
    // dichiarazione del service degli appuntamenti
    private final AppuntamentoService appuntamentoService;
    // dichiarazione del service degli utenti
    private final UtenteService utenteService;
    // dichiarazione della classe che effettua il managing delle ricerche
    private final RicercaManager ricercaManager;

    // endpoint per il manager per aggiungere un veicolo
    @PostMapping("/aggiungiVeicolo")
    public ResponseEntity<AggiuntaVeicoloResponseDTO> aggiungiVeicolo(
            @RequestParam("targa") String targa,
            @RequestParam("marca") String marca,
            @RequestParam("modello") String modello,
            @RequestParam("chilometraggio") Integer chilometraggio,
            @RequestParam("annoProduzione") Integer annoProduzione,
            @RequestParam("potenzaCv") Integer potenzaCv,
            @RequestParam("alimentazione") String alimentazione,
            @RequestParam("prezzo") Double prezzo,
            @RequestParam("immagine") MultipartFile immagine
            ) throws IOException {
        // creazione della richiesta per aggiungere un veicolo con i dati passati
        AggiuntaVeicoloRequestDTO request = new AggiuntaVeicoloRequestDTO(targa, marca, modello, chilometraggio, annoProduzione, potenzaCv, alimentazione, prezzo);
        // chiamata al metodo del service per aggiungere un veicolo
        veicoloService.aggiungiVeicolo(request, immagine);
        // ritorno la risposta creata tramite il DTO e il messaggio settato
        return ResponseEntity.status(HttpStatus.OK).body(new AggiuntaVeicoloResponseDTO("Veicolo aggiunto con successo"));
    }

    // endpoint per visualizzare i tutti veicoli
    @GetMapping("/veicoli")
    public ResponseEntity<List<VeicoloResponseDTO>> stampaVeicoli() throws IOException {
        // chiamata al metodo del service per visualizzare tutti i veicoli
        // creazione della risposta tramite il DTO e i dati tornati dal service
        List<VeicoloResponseDTO> veicoli = veicoloService.findAll();
        // ritorno la risposta con la lista di veicoli
        return ResponseEntity.status(HttpStatus.OK).body(veicoli);
    }

    // endpoint per visualizzare i dettagli di un veicolo in base all'id
    @GetMapping("/dettagli/{idVeicolo}")
    public ResponseEntity<DettagliVeicoloManagerResponseDTO> mostraDettagli(@PathVariable Long idVeicolo) throws IOException {
        // chiamata al metodo del service per recuperare i dettagli di un veicolo in base all'id
        // creazione della risposta tramite il DTO e i dati tornati dal service
        DettagliVeicoloManagerResponseDTO dettagli= veicoloService.recuperaDettagli(idVeicolo);
        // ritorno la risposta con i dettagli del veicolo
        return ResponseEntity.status(HttpStatus.OK).body(dettagli);
    }

    // endpoint per cercare i veicoli
    @PostMapping("/cerca")
    public ResponseEntity<List<VeicoloResponseDTO>> cercaVeicoli(@RequestBody RicercaRequestDTO request) throws IOException {
        // dichiarazione della lista di veicoli trovati
        List<Veicolo> veicoliTrovati;
        // dichiarazione della risposta tramite il DTO
        List<VeicoloResponseDTO> veicoliResponse;
        // chiamata al metodo per scegliere la strategia di ricerca in base al criterio passato nella richiesta
        RicercaStrategy ricercaStrategy = ricercaManager.scegliRicerca(request.getCriterio());
        // chiamata al metodo per cercare i veicoli in base alla strategia scelta e alla richiesta
        veicoliTrovati = ricercaManager.executeRicerca(ricercaStrategy,request);
        // chiamata al metodo del service per convertire i veicoli trovati in veicoliResponse
        // assegno il risultato alla rsiposta
        veicoliResponse = veicoloService.convertiVeicoliInVeicoliResponse(veicoliTrovati);
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(veicoliResponse);
    }

    // endpoint per la registrazione di una vendita
    @PostMapping("/registraVendita")
    public ResponseEntity<RegistrazioneVenditaResponseDTO> registraVendita(@RequestBody RegistrazioneVenditaRequestDTO request, @RequestHeader("Authorization") String token) {
        // dichiarazione della risposta tramite il DTO
        RegistrazioneVenditaResponseDTO response = new RegistrazioneVenditaResponseDTO();
        // controllo se la vendita è conclusa in base alla richiesta
        if(request.isVenditaConclusa()){
            // se la vendita è stata conclusa
            // recupero l'id del veicolo e del cliente in base all'id dell'appuntamento passato nella richiesta
            long idVeicolo = appuntamentoService.trovaIdVeicolo(request.getIdAppuntamento(), token);
            long idCliente = appuntamentoService.trovaIdCliente(request.getIdAppuntamento(), token);
            // recupero l'utente acquirente in base all'id del cliente trovato prima
            Utente acquirente = utenteService.findById(idCliente);
            // chiamata al metodo del service per registrare la vendita come effettuata
            appuntamentoService.registraVendita(request.getIdAppuntamento(), true, token);
            // chiamata al metodo del service per registrare la vendita del veicolo
            veicoloService.registraVendita(idVeicolo, acquirente);
            // setto il messaggio di risposta
            response.setMessaggio("Registrazione esito positivo effettuata con successo");

        } else {
            // se la vendita non è stata conclusa
            // chiamata al metodo del service per registrare la vendita come non effettuata
            appuntamentoService.registraVendita(request.getIdAppuntamento(), false, token);
            // setto il messaggio di risposta
            response.setMessaggio("Registrazione esito negativo effettuata con successo");
        }
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // endpoint per il manager per visualizzare i veicoli con i dettagli
    @GetMapping("/veicoliConDettagli")
    public ResponseEntity<List<DettagliVeicoloManagerResponseDTO>> stampaVeicoliConDettagli() throws IOException {
        // chiamata al metodo del service per visualizzare i veicoli con i dettagli
        // creazione della risposta tramite il DTO e i dati tornati dal service
        List<DettagliVeicoloManagerResponseDTO> veicoliConDettagli = veicoloService.findAllConDettagli();
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(veicoliConDettagli);
    }

    // endpoint per il manager per eliminare un veicolo in base all'id
    @DeleteMapping("/elimina/{idVeicolo}")
    public ResponseEntity<EliminaVeicoloResponseDTO> eliminaVeicolo(@PathVariable Long idVeicolo) {
        // chiamata al metodo del service per eliminare un veicolo in base all'id
        veicoloService.eliminaVeicolo(idVeicolo);
        // ritorno la risposta creata tramite il DTO e il messaggio settato
        return ResponseEntity.status(HttpStatus.OK).body(new EliminaVeicoloResponseDTO("Veicolo eliminato con successo"));
    }

    // endpoint per il manager per modificare un veicolo in base all'id
    @PutMapping("/modifica/{idVeicolo}")
    public ResponseEntity<ModificaVeicoloResponseDTO> modificaVeicolo(@PathVariable Long idVeicolo, @RequestBody ModificaVeicoloRequestDTO request) {
        // chiamata al metodo del service per modificare un veicolo in base all'id e alla richiesta
        veicoloService.modificaVeicolo(idVeicolo, request);
        // ritorno la risposta creata tramite il DTO e il messaggio settato
        return ResponseEntity.status(HttpStatus.OK).body(new ModificaVeicoloResponseDTO("Veicolo modificato con successo"));
    }

    // endpoint per il manager per visualizzare i veicoli disponibili
    @GetMapping("/veicoliDisponibili")
    public ResponseEntity<List<DettagliVeicoloManagerResponseDTO>> stampaVeicoliDiponibiliPerManager() throws IOException {
        // chiamata al metodo del service per visualizzare i veicoli disponibili
        // creazione della risposta tramite il DTO e i dati tornati dal service
        List<DettagliVeicoloManagerResponseDTO> veicoliDisponobili = veicoloService.findAllDisponibili();
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(veicoliDisponobili);
    }

    // endpoint per il manager per visualizzare i veicoli disponibili e quello seleionato per la modifica di un appuntamento in base all'id
    @GetMapping("/veicoliDisponibiliESelezionato/{idAppuntamento}")
    public ResponseEntity<List<DettagliVeicoloManagerResponseDTO>> stampaVeicoliDiponibiliESelezionatoPerManager(@PathVariable Long idAppuntamento) throws IOException {
        // chiamata al metodo del service per visualizzare i veicoli disponibili e quello selezionato in base all'id dell'appuntamento
        // creazione della risposta tramite il DTO e i dati tornati dal service
        List<DettagliVeicoloManagerResponseDTO> veicoliDisponobili = veicoloService.findAllDisponibiliESelezionato(idAppuntamento);
        // ritorno la risposta
        return ResponseEntity.status(HttpStatus.OK).body(veicoliDisponobili);
    }
}
