package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.*;
import com.polimi.carzone.dto.response.*;
import com.polimi.carzone.exception.*;
import com.polimi.carzone.model.Appuntamento;
import com.polimi.carzone.model.Ruolo;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.repository.AppuntamentoRepository;
import com.polimi.carzone.persistence.repository.UtenteRepository;
import com.polimi.carzone.persistence.repository.VeicoloRepository;
import com.polimi.carzone.persistence.service.AppuntamentoService;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.security.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

//annotazione per indicare a Spring che la classe è un service
@Service
//annotazione per indicare che i metodi della classe sono transazionali
//vengono eseguiti eseguiti all'interno di una transazione
@Transactional
//annotazione per generare un costruttore con un parametro per ciascun campo
@RequiredArgsConstructor
public class AppuntamentoServiceImpl implements AppuntamentoService {
    //la classe implementa i metodi dichiarati nell'interfaccia AppuntamentoService

    //dichiarazione della repository per gli appuntamenti
    private final AppuntamentoRepository appuntamentoRepo;
    //dichiarazione della repository per gli utenti
    private final UtenteRepository utenteRepo;
    //dichiarazione della repository per i veicoli
    private final VeicoloRepository veicoloRepo;
    //dichiarazione della classe TokenUtil, per gestire i token
    private final TokenUtil tokenUtil;
    //dichiarazione del service per i veicoli
    private final VeicoloService veicoloService;

    //metodo che a partire da una richiesta e un token, prenota un appuntamento
    @Override
    public void prenota(PrenotazioneRequestDTO request, String token) throws IOException {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();

        //controlli sui campi della request
        //se la request è null, viene inserito un errore nella mappa
        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        //se la data e l'ora della request sono null, precedenti alla data attuale o uguali alla data attuale, viene inserito un errore nella mappa
        if(request.getDataOra() == null || request.getDataOra().toLocalDate().isBefore(LocalDate.now()) || request.getDataOra().toLocalDate().isEqual(LocalDate.now())) {
            errori.put("dataOra", "La data deve essere almeno il giorno successivo a quello attuale");
            throw new CredenzialiNonValideException(errori);
        }

        //inserisco in variabili l'ora e il minuto della data e dell'ora della request
        int ora = request.getDataOra().getHour();
        int minuto = request.getDataOra().getMinute();

        //se l'ora è minore di 9, maggiore di 17 o uguale a 17 e il minuto è maggiore di 0, viene inserito un errore nella mappa
        if(ora < 9 || ora > 17 || (ora == 17 && minuto > 0)) {
            errori.put("dataOra", "Gli appuntamenti possono essere prenotati solo dalle 9:00 alle 17:00");
        }

        //se l'id del veicolo è null o minore o uguale a 0, viene inserito un errore nella mappa
        if(request.getIdVeicolo() == null || request.getIdVeicolo() <= 0) {
            errori.put("idVeicolo", "L'id del veicolo non è valido");
        }

        //se l'id del cliente è null o minore o uguale a 0, viene inserito un errore nella mappa
        if(request.getIdCliente() == null || request.getIdCliente() <= 0) {
            errori.put("idCliente", "L'id del cliente non è valido");
        }

        //se la mappa degli errori non è vuota, viene lanciata un'eccezione con la mappa degli errori
        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //trovo tutti i veicoli disponibili
        List<DettagliVeicoloManagerResponseDTO> veicoliDisponibili = veicoloService.findAllDisponibili();
        //estraggo gli id dei veicoli disponibili
        List<Long> veicoliDisponibiliId = veicoloService.estraiIdDaFindAllDisponibili(veicoliDisponibili);
        //se l'id del veicolo della request non è contenuto nella lista degli id dei veicoli disponibili, viene lanciata un'eccezione
        if(!veicoliDisponibiliId.contains(request.getIdVeicolo())){
            throw new VeicoloNonTraDisponibiliException("Il veicolo non è disponibile");
        }

        //se l'id del cliente non corrisponde all'id dell'utente loggato, viene lanciata un'eccezione
        if(request.getIdCliente() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del cliente non corrisponde all'id dell'utente loggato");
        }

        //trovo il cliente dall'id del cliente della request
        Optional<Utente> cliente = utenteRepo.findById(request.getIdCliente());
        //trovo il veicolo dall'id del veicolo della request
        Optional<Veicolo> veicolo = veicoloRepo.findById(request.getIdVeicolo());
        //se il cliente non è presente, viene lanciata un'eccezione
        if (cliente.isEmpty()){
            throw new UtenteNonTrovatoException("Cliente non trovato");
        }
        //se il veicolo non è presente, viene lanciata un'eccezione
        if(veicolo.isEmpty()){
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }
        //se sono stati superati tutti i controlli, creo un nuovo appuntamento
        Appuntamento appuntamento = new Appuntamento();
        //setto tutti i parametri dell'appuntamento
        appuntamento.setDataOra(request.getDataOra());
        appuntamento.setCliente(cliente.get());
        appuntamento.setVeicolo(veicolo.get());
        appuntamento.setEsitoRegistrato(false);
        //salvo l'appuntamento sul database
        appuntamentoRepo.save(appuntamento);
    }

    //metodo che a partire da un id e un token, trova gli appuntamenti del dipendente corrispondente all'id
    @Override
    public List<AppuntamentoResponseDTO> trovaAppuntamentiDipendente(Long idDipendente, String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //dichiarazione di una lista di appuntamenti tramite il DTO
        List<AppuntamentoResponseDTO> appuntamenti = new ArrayList<>();

        //controllo i parametri passati
        //se l'id del dipendente è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idDipendente == null || idDipendente <= 0){
            errori.put("idDipendente", "L'id del dipendente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //recupero il dipendente dal token e controllo che l'id del dipendente corrisponda all'id del dipendente passato
        //se non corrispondono, viene lanciata un'eccezione
        if(idDipendente != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }

        //trovo tutti gli appuntamenti del dipendente corrispondente all'id passato non ancora registrati
        //vengono inseriti nella lista di appuntamenti trovati
        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByDipendente_IdAndEsitoRegistratoIsFalse(idDipendente);
        //controllo che la lista di appuntamenti trovati non sia vuota
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()) {
            //se non è vuota, per ogni appuntamento trovato, creo un nuovo appuntamento tramite il DTO
            for (Appuntamento appuntamento : appuntamentiTrovati.get()) {
                AppuntamentoResponseDTO appuntamentoDipendente = new AppuntamentoResponseDTO();
                //setto tutti i parametri dell'appuntamento
                appuntamentoDipendente.setId(appuntamento.getId());
                appuntamentoDipendente.setDataOra(appuntamento.getDataOra());
                appuntamentoDipendente.setNomeCliente(appuntamento.getCliente().getNome());
                appuntamentoDipendente.setCognomeCliente(appuntamento.getCliente().getCognome());
                appuntamentoDipendente.setTargaVeicolo(appuntamento.getVeicolo().getTarga());
                appuntamentoDipendente.setMarcaVeicolo(appuntamento.getVeicolo().getMarca());
                appuntamentoDipendente.setModelloVeicolo(appuntamento.getVeicolo().getModello());
                appuntamentoDipendente.setDataPassata(appuntamento.getDataOra().isBefore(LocalDateTime.now()));
                //aggiungo l'appuntamento alla lista di appuntamenti da restituire
                appuntamenti.add(appuntamentoDipendente);
            }
        }
        //restituisco la lista di appuntamenti
        return appuntamenti;
    }

    //metodo che a partire da un id e un token, trova gli appuntamenti del cliente corrispondente all'id
    @Override
    public List<AppuntamentoConRecensioneResponseDTO> trovaAppuntamentiCliente(Long idCliente, String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //dichiarazione di una lista di appuntamenti tramite il DTO
        List<AppuntamentoConRecensioneResponseDTO> appuntamenti = new ArrayList<>();

        //controllo i parametri passati
        //se l'id del cliente è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idCliente == null || idCliente <= 0){
            errori.put("idCliente", "L'id del cliente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //recupero il cliente dal token e controllo che l'id del cliente corrisponda all'id del cliente passato
        //se non corrispondono, viene lanciata un'eccezione
        if(idCliente != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del cliente non corrisponde all'id dell'utente loggato");
        }

        //trovo tutti gli appuntamenti del cliente corrispondente all'id passato con i campi della recensione vuoti
        //vengono inseriti nella lista di appuntamenti trovati
        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByCliente_IdAndRecensioneVotoIsNullAndRecensioneTestoIsNull(idCliente);
        //controllo che la lista di appuntamenti trovati non sia vuota
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()) {
            //se non è vuota, per ogni appuntamento trovato, creo un nuovo appuntamento tramite il DTO
            for (Appuntamento appuntamento : appuntamentiTrovati.get()) {
                AppuntamentoConRecensioneResponseDTO appuntamentoCliente = new AppuntamentoConRecensioneResponseDTO();
                //setto tutti i parametri dell'appuntamento
                appuntamentoCliente.setId(appuntamento.getId());
                appuntamentoCliente.setDataOra(appuntamento.getDataOra());
                appuntamentoCliente.setNomeCliente(appuntamento.getCliente().getNome());
                appuntamentoCliente.setCognomeCliente(appuntamento.getCliente().getCognome());
                //se il dipendente non è null, setto il nome e il cognome del dipendente altimenti li lascio vuoti
                if(appuntamento.getDipendente() != null) {
                    appuntamentoCliente.setNomeDipendente(appuntamento.getDipendente().getNome());
                    appuntamentoCliente.setCognomeDipendente(appuntamento.getDipendente().getCognome());
                }
                appuntamentoCliente.setTargaVeicolo(appuntamento.getVeicolo().getTarga());
                appuntamentoCliente.setMarcaVeicolo(appuntamento.getVeicolo().getMarca());
                appuntamentoCliente.setModelloVeicolo(appuntamento.getVeicolo().getModello());
                appuntamentoCliente.setDataPassata(appuntamento.getDataOra().isBefore(LocalDateTime.now()));
                //aggiungo l'appuntamento alla lista di appuntamenti da restituire
                                                                                                                                appuntamenti.add(appuntamentoCliente);
            }
        }
        //restituisco la lista di appuntamenti
        return appuntamenti;
    }

    //metodo che a partire da un id e un token, calcola la valutazione media del dipendente corrispondente all'id
    @Override
    public ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(Long idDipendente, String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //dichiaro la risposta della valutazione media tramite il DTO
        ValutazioneMediaResponseDTO valutazioneMediaResponse = new ValutazioneMediaResponseDTO();
        //dichiaro la variabile della valutazione media e la inizializzo a 0
        double valutazioneMedia = 0.0;

        //controllo i parametri passati
        //se l'id del dipendente è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idDipendente == null || idDipendente <= 0){
            errori.put("idDipendente", "L'id del dipendente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //recupero il ruolo dell'utente dal token
        Ruolo ruoloUtente = tokenUtil.getUtenteFromToken(token.substring(7)).getRuolo();
        //controllo che il ruolo dell'utente sia diverso da manager e che l'id del dipendente corrisponda all'id dell'utente loggato
        //se non corrispondono, viene lanciata un'eccezione
        if(ruoloUtente != Ruolo.MANAGER && idDipendente != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }

        //trovo tutti gli appuntamenti del dipendente corrispondente all'id passato con la valutazione non nulla
        //vengono inseriti nella lista di appuntamenti trovati
        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNull(idDipendente);
        //controllo che la lista di appuntamenti trovati non sia vuota
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()){
            //se non è vuota, calcolo la somma delle valutazioni
            double sommaValutazioni = 0.0;
            //per ogni appuntamento trovato, se la valutazione non è nulla, aggiungo la valutazione alla somma delle valutazioni
            for(Appuntamento appuntamento : appuntamentiTrovati.get()){
                if(appuntamento.getRecensioneVoto() != null){
                    sommaValutazioni += appuntamento.getRecensioneVoto();
                }
            }
            //calcolo la valutazione media
            valutazioneMedia = sommaValutazioni / appuntamentiTrovati.get().size();
            //setto la valutazione media nella risposta
            valutazioneMediaResponse.setValutazioneMedia(valutazioneMedia);
        } else {
            //se la lista di appuntamenti trovati è vuota, setto la valutazione media a 0
            valutazioneMediaResponse.setValutazioneMedia(0.0);
        }
        //restituisco la risposta della valutazione media
        return valutazioneMediaResponse;
    }

    //metodo per il manager che a partire da un id, calcola la valutazione media del dipendente corrispondente all'id
    @Override
    public ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(Long idDipendente) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //dichiaro la risposta della valutazione media tramite il DTO
        ValutazioneMediaResponseDTO valutazioneMediaResponse = new ValutazioneMediaResponseDTO();
        //dichiaro la variabile della valutazione media e la inizializzo a 0
        double valutazioneMedia = 0.0;

        //controllo i parametri passati
        //se l'id del dipendente è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idDipendente == null || idDipendente <= 0){
            throw new CredenzialiNonValideException(errori);
        }

        //trovo tutti gli appuntamenti del dipendente corrispondente all'id passato con la valutazione non nulla
        //vengono inseriti nella lista di appuntamenti trovati
        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNull(idDipendente);
        //controllo che la lista di appuntamenti trovati non sia vuota
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()){
            //se non è vuota, calcolo la somma delle valutazioni
            double sommaValutazioni = 0.0;
            //per ogni appuntamento trovato, se la valutazione non è nulla, aggiungo la valutazione alla somma delle valutazioni
            for(Appuntamento appuntamento : appuntamentiTrovati.get()){
                if(appuntamento.getRecensioneVoto() != null){
                    sommaValutazioni += appuntamento.getRecensioneVoto();
                }
            }
            //calcolo la valutazione media
            valutazioneMedia = sommaValutazioni / appuntamentiTrovati.get().size();
            //setto la valutazione media nella risposta
            valutazioneMediaResponse.setValutazioneMedia(valutazioneMedia);
        } else {
            //se la lista di appuntamenti trovati è vuota, setto la valutazione media a 0
            valutazioneMediaResponse.setValutazioneMedia(0.0);
        }
        //restituisco la risposta della valutazione media
        return valutazioneMediaResponse;
    }

    //metodo che trova gli appuntamenti liberi (non ancora presi in carico da un dipendente)
    @Override
    public List<AppuntamentoResponseDTO> trovaAppuntamentiLiberi() {
        //dichiarazione di una lista di appuntamenti tramite il DTO
        List<AppuntamentoResponseDTO> appuntamentiLiberi = new ArrayList<>();
        //trovo tutti gli appuntamenti non presi in carico da un dipendente e li inserisco nella lista di appuntamenti liberi
        Optional<List<Appuntamento>> appuntamenti = appuntamentoRepo.findByDipendenteIsNull();
        //controllo che la lista di appuntamenti non sia vuota
        if(appuntamenti.isPresent() && !appuntamenti.get().isEmpty()) {
            //se non è vuota, per ogni appuntamento trovato, creo un nuovo appuntamento tramite il DTO
            for (Appuntamento appuntamento : appuntamenti.get()) {
                AppuntamentoResponseDTO appuntamentoLiberi = new AppuntamentoResponseDTO();
                //setto tutti i parametri dell'appuntamento
                appuntamentoLiberi.setId(appuntamento.getId());
                appuntamentoLiberi.setDataOra(appuntamento.getDataOra());
                appuntamentoLiberi.setNomeCliente(appuntamento.getCliente().getNome());
                appuntamentoLiberi.setCognomeCliente(appuntamento.getCliente().getCognome());
                appuntamentoLiberi.setTargaVeicolo(appuntamento.getVeicolo().getTarga());
                appuntamentoLiberi.setMarcaVeicolo(appuntamento.getVeicolo().getMarca());
                appuntamentoLiberi.setModelloVeicolo(appuntamento.getVeicolo().getModello());
                //aggiungo l'appuntamento alla lista di appuntamenti da restituire
                appuntamentiLiberi.add(appuntamentoLiberi);
            }
        }
        //restituisco la lista di appuntamenti
        return appuntamentiLiberi;
    }

    //metodo che a partire da una richiesta e un token, permette al dipendente di prendere in carico un appuntamento
    @Override
    public void prendiInCarico(PresaInCaricoRequestDTO request, String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();

        //controllo i parametri passati
        //se la request è null, viene inserito un errore nella mappa e lanciata un'eccezione
        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        //se l'id dell'appuntamento è null o minore o uguale a 0, viene inserito un errore nella mappa
        if(request.getIdAppuntamento() == null || request.getIdAppuntamento() <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
        }

        //se l'id del dipendente è null o minore o uguale a 0, viene inserito un errore nella mappa
        if(request.getIdDipendente() == null || request.getIdDipendente() <= 0){
            errori.put("idDipendente", "L'id del dipendente non è valido");
        }

        //se la mappa degli errori non è vuota, viene lanciata un'eccezione con la mappa
        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }


        if(request.getIdDipendente() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }

        //trovo l'appuntamento dall'id dell'appuntamento della request
        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(request.getIdAppuntamento());
        //trovo il dipendente dall'id del dipendente della request
        Optional<Utente> dipendente = utenteRepo.findById(request.getIdDipendente());
        //se l'appuntamento non è presente, viene lanciata un'eccezione
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        //se il dipendente non è presente, viene lanciata un'eccezione
        if(dipendente.isEmpty()){
            throw new UtenteNonTrovatoException("Dipendente non trovato");
        }
        //se l'appuntamento è già stato preso in carico, viene lanciata un'eccezione
        if(appuntamento.get().getDipendente() != null){
            throw new AppuntamentoPresoInCaricoException("L'appuntamento è già stato preso in carico");
        }
        //se l'appuntamento è già passato, viene lanciata un'eccezione
        if(appuntamento.get().getDataOra().isBefore(LocalDateTime.now())){
            throw new AppuntamentoPassatoException("L'appuntamento è già passato");
        }

        //se sono stati superati tutti i controlli, setto il dipendente dell'appuntamento e salvo l'appuntamento sul database
        appuntamento.get().setDipendente(dipendente.get());
        appuntamentoRepo.save(appuntamento.get());
    }

    //metodo che a partire da un id di un appuntamento e un token, trova l'id del veicolo associato all'appuntamento
    @Override
    public long trovaIdVeicolo(Long idAppuntamento, String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();

        //controllo i parametri passati
        //se l'id dell'appuntamento è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idAppuntamento == null || idAppuntamento <= 0){
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //trovo l'appuntamento dall'id dell'appuntamento passato
        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        //se l'appuntamento non è presente, viene lanciata un'eccezione
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        //se l'appuntamento ha un dipendente e l'id del dipendente non corrisponde all'id dell'utente loggato, viene lanciata un'eccezione
        if(appuntamento.get().getDipendente() != null && appuntamento.get().getDipendente().getId() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }
        //restituisco l'id del veicolo associato all'appuntamento
        return appuntamento.get().getVeicolo().getId();
    }

    //metodo che a partire da un id di un appuntamento e un token, trova l'id del cliente associato all'appuntamento
    @Override
    public long trovaIdCliente(Long idAppuntamento, String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();

        //controllo i parametri passati
        //se l'id dell'appuntamento è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idAppuntamento == null || idAppuntamento <= 0){
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //trovo l'appuntamento dall'id dell'appuntamento passato
        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        //se l'appuntamento non è presente, viene lanciata un'eccezione
        if(appuntamento.isPresent()){
            //se l'appuntamento ha un dipendente e l'id del dipendente non corrisponde all'id dell'utente loggato, viene lanciata un'eccezione
            if(appuntamento.get().getDipendente().getId() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
                throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
            }
            //restituisco l'id del cliente associato all'appuntamento
            return appuntamento.get().getCliente().getId();
        } else {
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
    }

    //metodo che a partire da un id di un dipendente e un token, trova tutte le recensioni associate al dipendente
    @Override
    public List<RecensioneResponseDTO> trovaRecensioniDipendente(Long idDipendente, String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //dichiarazione di una lista di recensioni tramite il DTO
        List<RecensioneResponseDTO> recensioni = new ArrayList<>();

        //controllo i parametri passati
        //se l'id del dipendente è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idDipendente == null || idDipendente <= 0){
            errori.put("id", "L'id del dipendente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //recupero il ruolo dell'utente dal token
        Ruolo ruoloUtente = tokenUtil.getUtenteFromToken(token.substring(7)).getRuolo();
        //controllo che il ruolo dell'utente sia diverso da manager e che l'id del dipendente corrisponda all'id dell'utente loggato
        //se non corrispondono, viene lanciata un'eccezione
        if(ruoloUtente != Ruolo.MANAGER && idDipendente != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }

        //trovo tutti gli appuntamenti del dipendente corrispondente all'id passato con la valutazione non nulla e il testo della recensione non nullo
        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(idDipendente);
        //controllo che la lista di appuntamenti trovati non sia vuota
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()){
            //se non è vuota, per ogni appuntamento trovato, creo un nuovo appuntamento tramite il DTO
            for(Appuntamento appuntamento : appuntamentiTrovati.get()){
                //setto tutti i parametri della recensione
                recensioni.add(new RecensioneResponseDTO(
                        appuntamento.getCliente().getNome(),
                        appuntamento.getCliente().getCognome(),
                        appuntamento.getRecensioneVoto(),
                        appuntamento.getRecensioneTesto()
                ));
            }
        }
        //restituisco la lista di recensioni
        return recensioni;
    }

    //metodo che a partire da un id di un dipendente, trova tutte le recensioni associate al dipendente
    @Override
    public List<RecensioneResponseDTO> trovaRecensioniDipendente(Long idDipendente) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //dichiarazione di una lista di recensioni tramite il DTO
        List<RecensioneResponseDTO> recensioni = new ArrayList<>();

        //controllo i parametri passati
        //se l'id del dipendente è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idDipendente == null || idDipendente <= 0){
            errori.put("id", "L'id del dipendente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //trovo tutti gli appuntamenti del dipendente corrispondente all'id passato con la valutazione non nulla e il testo della recensione non nullo
        //li inserisco nella lista di appuntamenti trovati
        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByDipendente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(idDipendente);
        //controllo che la lista di appuntamenti trovati non sia vuota
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()){
            //se non è vuota, per ogni appuntamento trovato, creo un nuovo appuntamento tramite il DTO
            for(Appuntamento appuntamento : appuntamentiTrovati.get()){
                //setto tutti i parametri della recensione
                recensioni.add(new RecensioneResponseDTO(
                        appuntamento.getCliente().getNome(),
                        appuntamento.getCliente().getCognome(),
                        appuntamento.getRecensioneVoto(),
                        appuntamento.getRecensioneTesto()
                ));
            }
        }
        //restituisco la lista di recensioni
        return recensioni;
    }

    //metodo che trova per ogni dipendente la valutazione media e tutte le recensioni associate
    @Override
    public List<DipendenteConRecensioneDTO> trovaDipendentiConRecensioni() {
        //dichiarazione di una lista di dipendenti con recensioni tramite il DTO
        List<DipendenteConRecensioneDTO> dipendentiConRecensioni = new ArrayList<>();
        //trovo tutti i dipendenti
        List<Utente> dipendenti = utenteRepo.findByRuolo(Ruolo.DIPENDENTE);
        //per ogni dipendente, trovo tutte le recensioni associate
        for(Utente dipendente : dipendenti){
            List<RecensioneResponseDTO> recensioni = trovaRecensioniDipendente(dipendente.getId());
            //se le recensioni non sono vuote, creo un nuovo dipendente con recensioni tramite il DTO
            //setto tutti i parametri del dipendente con recensioni e lo aggiungo alla lista di dipendenti con recensioni
            if(!recensioni.isEmpty()){
                dipendentiConRecensioni.add(new DipendenteConRecensioneDTO(
                        dipendente.getNome(),
                        dipendente.getCognome(),
                        calcolaValutazioneMediaDipendente(dipendente.getId()),
                        recensioni
                ));
            }
        }
        //restituisco la lista di dipendenti con recensioni
        return dipendentiConRecensioni;
    }

    //metodo che permette di registrare la vendita di un veicolo associato a un appuntamento
    @Override
    public void registraVendita(Long idAppuntamento, boolean venditaConclusa, String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();

        //controllo i parametri passati
        //se l'id dell'appuntamento è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idAppuntamento == null || idAppuntamento <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //trovo l'appuntamento dall'id dell'appuntamento passato
        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        //trovo il ruolo dell'utente dal token
        Ruolo ruoloUtente = tokenUtil.getUtenteFromToken(token.substring(7)).getRuolo();
        //se l'appuntamento non è presente, viene lanciata un'eccezione
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        //se il ruolo dell'utente è diverso da manager e l'id del dipendente dell'appuntamento non corrisponde all'id dell'utente loggato, viene lanciata un'eccezione
        if(ruoloUtente != Ruolo.MANAGER && appuntamento.get().getDipendente().getId() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del dipendente non corrisponde all'id dell'utente loggato");
        }
        //se l'appuntamento è nel futuro, viene lanciata un'eccezione
        if(appuntamento.get().getDataOra().isAfter(LocalDateTime.now())){
            throw new AppuntamentoNonSvoltoException("L'appuntamento non è ancora stato svolto");
        }
        //se l'appuntamento è già stato registrato, viene lanciata un'eccezione
        if(appuntamento.get().isEsitoRegistrato()){
            throw new AppuntamentoRegistratoException("L'esito dell'appuntamento è già stato registrato");
        }
        //se l'appuntamento non è stato preso in carico da un dipendente, viene lanciata un'eccezione
        if(appuntamento.get().getDipendente() == null){
            throw new AppuntamentoNonAssegnatoException("L'appuntamento non è stato preso in carico da nessun dipendente");
        }

        //se i controlli sono stati superati, setto la registrazione della vendita all'appuntamento e salvo l'appuntamento sul database
        appuntamento.get().setEsitoRegistrato(true);
        appuntamentoRepo.save(appuntamento.get());
    }

    //metodo per il manager che permette di trovare tutti gli appuntamenti
    @Override
    public List<AppuntamentoManagerResponseDTO> trovaAppuntamentiPerManager() {
        //dichiarazione di una lista di appuntamenti per il manager tramite il DTO
        List<AppuntamentoManagerResponseDTO> appuntamentiPerManager = new ArrayList<>();
        //trovo tutti gli appuntamenti e li inserisco nella lista di appuntamenti
        List<Appuntamento> appuntamenti = appuntamentoRepo.findAll();
        //per ogni appuntamento trovato
        for(Appuntamento appuntamento : appuntamenti){
            //se il dipendente non è null, creo un nuovo appuntamento per il manager tramite il DTO
            if(appuntamento.getDipendente() != null){
                appuntamentiPerManager.add(new AppuntamentoManagerResponseDTO(
                        //setto tutti i parametri dell'appuntamento
                        appuntamento.getId(),
                        appuntamento.getDataOra(),
                        appuntamento.getCliente().getNome(),
                        appuntamento.getCliente().getCognome(),
                        appuntamento.getDipendente().getNome(),
                        appuntamento.getDipendente().getCognome(),
                        appuntamento.getVeicolo().getTarga(),
                        appuntamento.getVeicolo().getMarca(),
                        appuntamento.getVeicolo().getModello(),
                        appuntamento.isEsitoRegistrato(),
                        appuntamento.getDataOra().isBefore(LocalDateTime.now())

                ));
            } else {
                //se il dipendente è null, creo un nuovo appuntamento per il manager tramite il DTO
                appuntamentiPerManager.add(new AppuntamentoManagerResponseDTO(
                        //setto tutti i parametri dell'appuntamento eccetto il dipendente
                        appuntamento.getId(),
                        appuntamento.getDataOra(),
                        appuntamento.getCliente().getNome(),
                        appuntamento.getCliente().getCognome(),
                        null,
                        null,
                        appuntamento.getVeicolo().getTarga(),
                        appuntamento.getVeicolo().getMarca(),
                        appuntamento.getVeicolo().getModello(),
                        appuntamento.isEsitoRegistrato(),
                        appuntamento.getDataOra().isBefore(LocalDateTime.now())
                ));
            }

        }
        //restituisco la lista di appuntamenti per il manager
        return appuntamentiPerManager;
    }

    //metodo per il manager che permette di prenotare un appuntamento
    @Override
    public void prenotaPerManager(PrenotazioneManagerRequestDTO request) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();

        //controllo i parametri passati
        //se la request è null, viene inserito un errore nella mappa e lanciata un'eccezione
        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        //se la data e l'ora della request è null o precedente a quella attuale, viene inserito un errore nella mappa
        if(request.getDataOra() == null || request.getDataOra().isBefore(LocalDateTime.now()) || request.getDataOra().isEqual(LocalDateTime.now())) {
            errori.put("dataOra", "La data e l'ora devono essere successive a quella attuale");
        }

        //se l'id del veicolo della request è null o minore o uguale a 0, viene inserito un errore nella mappa
        if(request.getIdVeicolo() == null || request.getIdVeicolo() <= 0) {
            errori.put("idVeicolo", "L'id del veicolo non è valido");
        }

        //se l'id del cliente della request è null o minore o uguale a 0, viene inserito un errore nella mappa
        if(request.getIdCliente() == null || request.getIdCliente() <= 0) {
            errori.put("idCliente", "L'id del cliente non è valido");
        }

        //se l'id del dipendente della request è null o minore di 0, viene inserito un errore nella mappa
        if(request.getIdDipendente() == null || request.getIdDipendente() < 0) {
            errori.put("idDipendente", "L'id del dipendente non è valido");
        }

        //se la mappa degli errori non è vuota, viene lanciata un'eccezione con la mappa
        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        //trovo il cliente dall'id del cliente della request
        Optional<Utente> cliente = utenteRepo.findById(request.getIdCliente());
        //trovo il veicolo dall'id del veicolo della request
        Optional<Veicolo> veicolo = veicoloRepo.findById(request.getIdVeicolo());
        //inizializzo un optional per il dipendente
        Optional<Utente> dipendente = Optional.empty();
        //se l'id del dipendente della request è diverso da 0 trovo il dipendente dall'id della request
        if(request.getIdDipendente() != 0){
            dipendente = utenteRepo.findById(request.getIdDipendente());
            //se il dipendente non è presente, viene lanciata un'eccezione
            if(dipendente.isEmpty()){
                throw new UtenteNonTrovatoException("Dipendente non trovato");
            }
        }
        //se il cliente non è presente, viene lanciata un'eccezione
        if (cliente.isEmpty()){
            throw new UtenteNonTrovatoException("Cliente non trovato");
        }
        //se il veicolo non è presente, viene lanciata un'eccezione
        if(veicolo.isEmpty()){
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }

        //se tutti i controlli sono stati superati, creo un nuovo appuntamento
        Appuntamento appuntamento = new Appuntamento();
        //setto tutti i parametri dell'appuntamento
        appuntamento.setDataOra(request.getDataOra());
        appuntamento.setCliente(cliente.get());
        appuntamento.setVeicolo(veicolo.get());
        //se l'id del dipendente della request è diverso da 0, setto il dipendente dell'appuntamento
        if(request.getIdDipendente() != 0){
            appuntamento.setDipendente(dipendente.get());
        }
        //settato la registrazione dell'esito dell'appuntamento a false
        appuntamento.setEsitoRegistrato(false);
        //salvo l'appuntamento sul database
        appuntamentoRepo.save(appuntamento);
    }

    //metodo per il manager che permette di eliminare un appuntamento
    @Override
    public void eliminaAppuntamento(Long idAppuntamento) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();

        //controllo i parametri passati
        //se l'id dell'appuntamento è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idAppuntamento == null || idAppuntamento <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //trovo l'appuntamento dall'id dell'appuntamento passato
        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        //se l'appuntamento non è presente, viene lanciata un'eccezione
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        //se l'appuntamento è già stato registrato, viene lanciata un'eccezione
        if(appuntamento.get().isEsitoRegistrato()){
            throw new AppuntamentoRegistratoException("L'esito dell'appuntamento è già stato registrato");
        }

        //se tutti i controlli sono stati superati, elimino l'appuntamento dal database
        appuntamentoRepo.delete(appuntamento.get());
    }

    //metodo per il manager che permette di modificare un appuntamento
    @Override
    public void modificaAppuntamento(Long idAppuntamento, ModificaAppuntamentoRequestDTO request) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();

        //controllo i parametri passati
        //se l'id dell'appuntamento è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idAppuntamento == null || idAppuntamento <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //se la request è null, viene inserito un errore nella mappa e lanciata un'eccezione
        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        //se la data e l'ora della request è null o precedente a quella attuale, viene inserito un errore nella mappa
        if(request.getDataOra() == null || request.getDataOra().isBefore(LocalDateTime.now()) || request.getDataOra().isEqual(LocalDateTime.now())) {
            errori.put("dataOra", "La data e l'ora devono essere successive a quella attuale");
        }

        //se l'id del veicolo della request è null o minore o uguale a 0, viene inserito un errore nella mappa
        if(request.getIdVeicolo() == null || request.getIdVeicolo() <= 0) {
            errori.put("idVeicolo", "L'id del veicolo non è valido");
        }

        //se l'id del cliente della request è null o minore o uguale a 0, viene inserito un errore nella mappa
        if(request.getIdCliente() == null || request.getIdCliente() <= 0) {
            errori.put("idCliente", "L'id del cliente non è valido");
        }

        //se l'id del dipendente della request è null o minore di 0, viene inserito un errore nella mappa
        if(request.getIdDipendente() != null && request.getIdDipendente() < 0) {
            errori.put("idDipendente", "L'id del dipendente non è valido");
        }

        //se la mappa degli errori non è vuota, viene lanciata un'eccezione con la mappa
        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        //trovo l'appuntamento dall'id dell'appuntamento passato
        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        //trovo il cliente dall'id del cliente passato
        Optional<Utente> cliente = utenteRepo.findById(request.getIdCliente());
        //trovo il veicolo dall'id del veicolo passato
        Optional<Veicolo> veicolo = veicoloRepo.findById(request.getIdVeicolo());
        //inizializzo un optional per il dipendente
        Optional<Utente> dipendente = Optional.empty();
        //se l'id del dipendente della request è diverso da null, trovo il dipendente dall'id della request
        if(request.getIdDipendente() != null){
            dipendente = utenteRepo.findById(request.getIdDipendente());
            //se il dipendente non è presente, viene lanciata un'eccezione
            if(dipendente.isEmpty()){
                throw new UtenteNonTrovatoException("Dipendente non trovato");
            }
        }
        //se l'appuntamento non è presente, viene lanciata un'eccezione
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }
        //se l'appuntamento è già stato registrato, viene lanciata un'eccezione
        if(appuntamento.get().isEsitoRegistrato()){
            throw new AppuntamentoRegistratoException("L'esito dell'appuntamento è già stato registrato");
        }
        //se il cliente non è presente, viene lanciata un'eccezione
        if(cliente.isEmpty()){
            throw new UtenteNonTrovatoException("Cliente non trovato");
        }

        //se il veicolo non è presente, viene lanciata un'eccezione
        if(veicolo.isEmpty()){
            throw new VeicoloNonTrovatoException("Veicolo non trovato");
        }

        //se tutti i controlli sono stati superati, modifico l'appuntamento con i nuovi parametri
        appuntamento.get().setDataOra(request.getDataOra());
        appuntamento.get().setCliente(cliente.get());
        appuntamento.get().setVeicolo(veicolo.get());
        //se l'id del dipendente della request è diverso da null, setto il dipendente dell'appuntamento
        if(request.getIdDipendente() != null){
            appuntamento.get().setDipendente(dipendente.get());
        }
        //salvo l'appuntamento sul database
        appuntamentoRepo.save(appuntamento.get());
    }

    //metodo che a partire da una richiesta e un token, permette al cliente di lasciare una recensione
    @Override
    public void lasciaRecensione(LasciaRecensioneRequestDTO request,String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();

        //controllo i parametri passati
        //se la request è null, viene inserito un errore nella mappa e lanciata un'eccezione
        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }

        //se l'id dell'appuntamento della request è null o minore o uguale a 0, viene inserito un errore nella mappa
        if(request.getIdAppuntamento()==null || request.getIdAppuntamento() <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
        }

        //se il voto della request è null o minore di 1 o maggiore di 5, viene inserito un errore nella mappa
        if(request.getVotoRecensione()==null || request.getVotoRecensione() < 1 || request.getVotoRecensione() > 5){
            errori.put("voto", "Il voto deve essere compreso tra 1 e 5");
        }

        //se il testo della request è null o vuoto o composto da spazi, viene inserito un errore nella mappa
        if(request.getTestoRecensione() == null || request.getTestoRecensione().isEmpty() || request.getTestoRecensione().isBlank()){
            errori.put("testo", "Il testo non può essere vuoto");
        }

        //se la mappa degli errori non è vuota, viene lanciata un'eccezione con la mappa
        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //trovo l'appuntamento dall'id dell'appuntamento della request
        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(request.getIdAppuntamento());
        //se l'appuntamento non è presente, viene lanciata un'eccezione
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }

        //recupero l'id del cliente loggato dal token
        //se l'appuntamento non è associato al cliente loggato, viene lanciata un'eccezione
        if(appuntamento.get().getCliente().getId() != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'appuntamento non è tuo");
        }

        //se l'appuntamento è nel futuro, viene lanciata un'eccezione
        if(!appuntamento.get().getDataOra().isBefore(LocalDateTime.now())) {
            throw new AppuntamentoNonSvoltoException("Appuntamento non ancora svolto");
        }

        //se l'appuntamento ha già una recensione, viene lanciata un'eccezione
        if(appuntamento.get().getRecensioneVoto() != null || appuntamento.get().getRecensioneTesto() != null){
            throw new RecensioneGiaLasciataException("Recensione già lasciata");
        }

        //se l'appuntamento non ha un dipendente associato, viene lanciata un'eccezione
        if(appuntamento.get().getDipendente() == null){
            throw new DipendenteNonAssegnatoException("Dipendente non assegnato");
        }

        //se tutti i controlli sono stati superati, setto la recensione all'appuntamento
        appuntamento.get().setRecensioneVoto(request.getVotoRecensione());
        appuntamento.get().setRecensioneTesto(request.getTestoRecensione());
        //salvo l'appuntamento sul database
        appuntamentoRepo.save(appuntamento.get());
    }

    //metodo che a partire da un id di un cliente e un token, trova tutte le recensioni associate al cliente
    @Override
    public List<RecensioneClienteResponseDTO> trovaRecensioniCliente(Long idCliente, String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //dichiarazione di una lista di recensioni per il cliente tramite il DTO
        List<RecensioneClienteResponseDTO> recensioni = new ArrayList<>();

        //controllo i parametri passati
        //se l'id del cliente è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idCliente == null || idCliente <= 0){
            errori.put("idCliente", "L'id del cliente non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //recupero l'id del cliente loggato dal token
        //se l'id del cliente passato non corrisponde all'id dell'utente loggato, viene lanciata un'eccezione
        if(idCliente != tokenUtil.getUtenteFromToken(token.substring(7)).getId()){
            throw new DiversiIdException("L'id del cliente non corrisponde all'id dell'utente loggato");
        }

        //trovo tutti gli appuntamenti del cliente corrispondente all'id passato con la valutazione non nulla e il testo della recensione non nullo
        Optional<List<Appuntamento>> appuntamentiTrovati = appuntamentoRepo.findByCliente_IdAndRecensioneVotoNotNullAndRecensioneTestoNotNull(idCliente);
        //controllo che la lista di appuntamenti trovati non sia vuota
        if(appuntamentiTrovati.isPresent() && !appuntamentiTrovati.get().isEmpty()){
            //se non è vuota, per ogni appuntamento trovato, creo un nuovo appuntamento tramite il DTO
            for(Appuntamento appuntamento : appuntamentiTrovati.get()){
                //setto tutti i parametri della recensione
                recensioni.add(new RecensioneClienteResponseDTO(
                        appuntamento.getDipendente().getNome(),
                        appuntamento.getDipendente().getCognome(),
                        appuntamento.getRecensioneVoto(),
                        appuntamento.getRecensioneTesto()
                ));
            }
        }
        //restituisco la lista di recensioni
        return recensioni;
    }

    //metodo per il manager che permette di trovare un appuntaemnto per la modifica a partire da un id e un token
    @Override
    public AppuntamentoModificaResponseDTO trovaPerModifica(Long idAppuntamento, String token) {
        //dichiarazione di una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //dichiarazione di un appuntamento per la modifica tramite il DTO
        AppuntamentoModificaResponseDTO appuntamentoModifica = new AppuntamentoModificaResponseDTO();

        //controllo i parametri passati
        //se l'id dell'appuntamento è null o minore o uguale a 0, viene inserito un errore nella mappa e lanciata un'eccezione
        if(idAppuntamento == null || idAppuntamento <= 0){
            errori.put("idAppuntamento", "L'id dell'appuntamento non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //se il token è null, vuoto o composto da spazi, viene lanciata un'eccezione
        if(token == null || token.isEmpty() || token.isBlank()){
            throw new TokenNonValidoException("Token non valido");
        }

        //trovo l'appuntamento dall'id dell'appuntamento passato
        Optional<Appuntamento> appuntamento = appuntamentoRepo.findById(idAppuntamento);
        //se l'appuntamento non è presente, viene lanciata un'eccezione
        if(appuntamento.isEmpty()){
            throw new AppuntamentoNonTrovatoException("Appuntamento non trovato");
        }

        //se l'appuntamento è già stato registrato, viene lanciata un'eccezione
        if(appuntamento.get().isEsitoRegistrato()){
            throw new AppuntamentoRegistratoException("L'esito dell'appuntamento è già stato registrato");
        }

        //se i controlli sono stati superati, setto tutti i parametri dell'appuntamento per la modifica
        appuntamentoModifica.setDataOra(appuntamento.get().getDataOra());
        appuntamentoModifica.setIdCliente(appuntamento.get().getCliente().getId());
        appuntamentoModifica.setNomeCliente(appuntamento.get().getCliente().getNome());
        appuntamentoModifica.setCognomeCliente(appuntamento.get().getCliente().getCognome());
        appuntamentoModifica.setIdVeicolo(appuntamento.get().getVeicolo().getId());
        appuntamentoModifica.setTargaVeicolo(appuntamento.get().getVeicolo().getTarga());
        appuntamentoModifica.setMarcaVeicolo(appuntamento.get().getVeicolo().getMarca());
        appuntamentoModifica.setModelloVeicolo(appuntamento.get().getVeicolo().getModello());
        //se il dipendente dell'appuntamento non è null, setto tutti i parametri del dipendente
        if(appuntamento.get().getDipendente() != null){
            appuntamentoModifica.setIdDipendente(appuntamento.get().getDipendente().getId());
            appuntamentoModifica.setNomeDipendente(appuntamento.get().getDipendente().getNome());
            appuntamentoModifica.setCognomeDipendente(appuntamento.get().getDipendente().getCognome());
        }
        //restituisco l'appuntamento per la modifica
        return appuntamentoModifica;
    }
}
