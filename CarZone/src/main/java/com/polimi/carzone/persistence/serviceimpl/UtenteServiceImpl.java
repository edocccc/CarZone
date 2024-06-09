package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.LoginRequestDTO;
import com.polimi.carzone.dto.request.ModificaUtenteRequestDTO;
import com.polimi.carzone.dto.request.SignupRequestDTO;
import com.polimi.carzone.dto.response.UtenteManagerResponseDTO;
import com.polimi.carzone.exception.CredenzialiNonValideException;
import com.polimi.carzone.exception.RuoloNonValidoException;
import com.polimi.carzone.exception.UtenteNonTrovatoException;
import com.polimi.carzone.exception.VeicoloNonTrovatoException;
import com.polimi.carzone.model.Ruolo;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.repository.UtenteRepository;
import com.polimi.carzone.persistence.service.UtenteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

//annotazione per indicare a Spring che la classe è un service
@Service
//annotazione per indicare che i metodi della classe sono transazionali
//vengono eseguiti eseguiti all'interno di una transazione
@Transactional
//annotazione per generare un costruttore con un parametro per ciascun campo
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService {
    //la classe implementa l'interfaccia UtenteService

    //campo per l'accesso al repository degli utenti
    private final UtenteRepository utenteRepo;
    //campo per l'accesso all'encoder delle password
    private final PasswordEncoder passwordEncoder;

    //metodo che a partire da un username, trova un utente
    @Override
    public Utente findByUsername(String username) {
        //trovo l'utente con lo username passato come parametro
        Optional<Utente> utente = utenteRepo.findByUsername(username);
        //se l'utente è presente, lo restituisco
        if(utente.isPresent()) {
            return utente.get();
        } else {
            //altrimenti lancio un'eccezione
            throw new UtenteNonTrovatoException("Utente non trovato");
        }
    }

    //metodo che a partire da una richiesta, permette il login di un utente
    @Override
    public Utente login(LoginRequestDTO request) {
        //dichiara una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();

        //controlli sui campi della request
        //se la request è null, aggiungo un errore alla mappa e lancio un'eccezione
        if(request == null) {
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }
        //se lo username è null, vuoto o contiene solo spazi, aggiungo un errore alla mappa
        if(request.getUsername() == null || request.getUsername().isEmpty() || request.getUsername().isBlank()) {
            errori.put("username", "Devi inserire uno username valido");
        }
        //se la password è null, vuota o contiene solo spazi, aggiungo un errore alla mappa
        if(request.getPassword() == null || request.getPassword().isEmpty() || request.getPassword().isBlank()){
            errori.put("password", "Devi inserire una password valida");
        }
        //se la mappa degli errori non è vuota, lancio un'eccezione
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        //cerco l'utente con lo username passato come parametro
        Optional<Utente> utente = utenteRepo.findByUsername(request.getUsername());
        //se l'utente non è presente, lancio un'eccezione
        if(utente.isEmpty()) {
            throw new UtenteNonTrovatoException("Non ci sono utenti con queste credenziali");
        }
        //se la password passata come parametro non corrisponde alla password dell'utente, lancio un'eccezione
        if(!passwordEncoder.matches(request.getPassword(), utente.get().getPassword())) {
            throw new UtenteNonTrovatoException("Non ci sono utenti con queste credenziali");
        }
        //se tutti i controlli sono stati superati, restituisco l'utente
        return utente.get();
    }

    //metodo che a partire da una richiesta, permette la registrazione di un cliente
    @Override
    public void registrazioneCliente(SignupRequestDTO request) {
        //controllo i campi della request
        ControllaSignupRequest(request);

        //creo un nuovo utente
        Utente utente = new Utente();
        //setto i campi dell'utente con i valori della request
        utente.setEmail(request.getEmail());
        utente.setNome(request.getNome());
        utente.setCognome(request.getCognome());
        utente.setDataNascita(request.getDataNascita());
        utente.setUsername(request.getUsername());
        //cripto la password e la setto come password dell'utente
        utente.setPassword(passwordEncoder.encode(request.getPassword()));
        //setto il ruolo dell'utente come cliente
        utente.setRuolo(Ruolo.CLIENTE);
        //salvo l'utente nel database
        utenteRepo.save(utente);
    }

    //metodo che a partire da una richiesta, permette la registrazione di un dipendente ad un manager
    @Override
    public void registrazioneDipendente(SignupRequestDTO request) {
        //controllo i campi della request
        ControllaSignupRequest(request);

        //creo un nuovo utente
        Utente utente = new Utente();
        //setto i campi dell'utente con i valori della request
        utente.setEmail(request.getEmail());
        utente.setNome(request.getNome());
        utente.setCognome(request.getCognome());
        utente.setDataNascita(request.getDataNascita());
        utente.setUsername(request.getUsername());
        //cripto la password e la setto come password dell'utente
        utente.setPassword(passwordEncoder.encode(request.getPassword()));
        //setto il ruolo dell'utente come dipendente
        utente.setRuolo(Ruolo.DIPENDENTE);
        //salvo l'utente nel database
        utenteRepo.save(utente);
    }

    //metodo che a partire da un id, trova un utente
    @Override
    public Utente findById(long id) {
        //dichiaro una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i parametri passati
        //se l'id è minore o uguale a 0, aggiungo un errore alla mappa e lancio un'eccezione
        if(id <= 0){
            errori.put("id", "L'id dell'utente non è valido");
            throw new CredenzialiNonValideException(errori);
        }
        //cerco l'utente con l'id passato come parametro
        Optional<Utente> utente = utenteRepo.findById(id);
        //se l'utente è presente, lo restituisco
        if(utente.isPresent()) {
            return utente.get();
        } else {
            //altrimenti lancio un'eccezione
            throw new UtenteNonTrovatoException("Utente non trovato");
        }
    }

    //metodo per il manager che trova tutti gli utenti
    @Override
    public List<UtenteManagerResponseDTO> trovaUtentiManager() {
        //trovo tutti gli utenti
        List<Utente> utenti = utenteRepo.findAll();
        //creo una lista di utenti da restituire tramite il DTO
        List<UtenteManagerResponseDTO> utentiResponse = new ArrayList<>();
        //per ogni utente trovato, creo un DTO
        for (Utente utente : utenti) {
            UtenteManagerResponseDTO utenteResponse = new UtenteManagerResponseDTO();
            //setto i campi del DTO con i valori dell'utente
            utenteResponse.setId(utente.getId());
            utenteResponse.setEmail(utente.getEmail());
            utenteResponse.setNome(utente.getNome());
            utenteResponse.setCognome(utente.getCognome());
            utenteResponse.setDataNascita(utente.getDataNascita());
            utenteResponse.setUsername(utente.getUsername());
            utenteResponse.setRuolo(utente.getRuolo());
            //aggiungo il DTO alla lista da restituire
            utentiResponse.add(utenteResponse);
        }
        //restituisco la lista
        return utentiResponse;
    }

    //metodo per il manager che a partire da un id, trova un utente
    @Override
    public UtenteManagerResponseDTO trovaUtenteManager(Long id) {
        //dichiaro una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i parametri passati
        //se l'id è minore o uguale a 0, aggiungo un errore alla mappa e lancio un'eccezione
        if(id == null || id <= 0){
            errori.put("id", "L'id non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        //cerco l'utente con l'id passato come parametro
        Optional<Utente> utente = utenteRepo.findById(id);
        //se l'utente non è presente, lancio un'eccezione
        if(utente.isEmpty()){
            throw new UtenteNonTrovatoException("Utente non trovato");
        }
        //creo un DTO e setto i campi con i valori dell'utente
        UtenteManagerResponseDTO utenteResponse = new UtenteManagerResponseDTO();
        utenteResponse.setId(utente.get().getId());
        utenteResponse.setEmail(utente.get().getEmail());
        utenteResponse.setNome(utente.get().getNome());
        utenteResponse.setCognome(utente.get().getCognome());
        utenteResponse.setDataNascita(utente.get().getDataNascita());
        utenteResponse.setUsername(utente.get().getUsername());
        utenteResponse.setRuolo(utente.get().getRuolo());
        //restituisco il DTO
        return utenteResponse;
    }

    //metodo per il manager che a partire da una richiesta, modifica un utente
    @Override
    public void modificaUtente(Long idUtente, ModificaUtenteRequestDTO request) {
        //dichiaro una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i parametri passati
        //se la request è null, aggiungo un errore alla mappa e lancio un'eccezione
        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }
        //se l'id dell'utente è minore o uguale a 0, aggiungo un errore alla mappa
        if(idUtente == null || idUtente <= 0){
            errori.put("idUtente", "L'id dell'utente non è valido");
        }
        //se l'email è null, vuota o non contiene '@' e '.', aggiungo un errore alla mappa
        if(request.getEmail()==null || request.getEmail().isEmpty() || !request.getEmail().contains("@") || !request.getEmail().contains(".")){
            errori.put("email", "Devi inserire una email valida");
        }
        //se il nome è null o vuoto, aggiungo un errore alla mappa
        if(request.getNome()==null || request.getNome().isEmpty()){
            errori.put("nome", "Devi inserire un nome valido");
        }
        //se il cognome è null o vuoto, aggiungo un errore alla mappa
        if(request.getCognome()==null || request.getCognome().isEmpty()){
            errori.put("cognome", "Devi inserire un cognome valido");
        }
        //se la data di nascita è null, è successiva alla data attuale o precedente al 1900, aggiungo un errore alla mappa
        if(request.getDataNascita()==null || request.getDataNascita().isAfter(LocalDate.now()) || request.getDataNascita().isBefore(LocalDate.of(1900,1,1))){
            errori.put("dataNascita", "Devi inserire una data di nascita valida");
        }
        //se lo username è null o vuoto, aggiungo un errore alla mappa
        if(request.getUsername()==null || request.getUsername().isEmpty()){
            errori.put("username", "Devi inserire uno username valido");
        }
        //inizializzo una variabile ruolo
        Ruolo ruolo = null;

        //se il ruolo passato è null o vuoto o contiene solo spazi, aggiungo un errore alla mappa
        if(request.getRuolo() == null || request.getRuolo().isEmpty() || request.getRuolo().isBlank()) {
            errori.put("ruolo", "Devi inserire un ruolo valido");
        } else {
            //assegno alla variabile ruolo il ruolo passato come parametro
            ruolo = switch (request.getRuolo()) {
                //se il ruolo non è uno dei valori contenuti nella classe Enum Ruolo, lancio un'eccezione, altrimenti assengo il ruolo alla variabile
                case "CLIENTE" -> Ruolo.CLIENTE;
                case "DIPENDENTE" -> Ruolo.DIPENDENTE;
                case "MANAGER" -> Ruolo.MANAGER;
                default -> throw new RuoloNonValidoException("Tipo di ruolo non valido");
            };
        }
        //se la mappa degli errori non è vuota, lancio un'eccezione
        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        //cerco l'utente con l'id passato come parametro
        Optional<Utente> utente = utenteRepo.findById(idUtente);
        //se l'utente non è presente, lancio un'eccezione
        if(utente.isEmpty()){
            throw new UtenteNonTrovatoException("Utente non trovato");
        }
        //se il ruolo dell'utente è manager, lancio un'eccezione
        if(utente.get().getRuolo().equals(Ruolo.MANAGER)){
            throw new RuoloNonValidoException("Non è possibile modificare un manager");
        }

        //se i controlli sono stati superati, modifico i campi dell'utente con i valori della request
        utente.get().setEmail(request.getEmail());
        utente.get().setNome(request.getNome());
        utente.get().setCognome(request.getCognome());
        utente.get().setDataNascita(request.getDataNascita());
        utente.get().setUsername(request.getUsername());
        utente.get().setRuolo(ruolo);
        //salvo l'utente nel database
        utenteRepo.save(utente.get());
    }

    //metodo per il manager che a partire da un id, elimina un utente
    @Override
    public void eliminaUtente(Long idUtente) {
        //dichiaro una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i parametri passati
        //se l'id è minore o uguale a 0, aggiungo un errore alla mappa e lancio un'eccezione
        if(idUtente == null || idUtente <= 0) {
            errori.put("id", "Id utente non valido");
            throw new CredenzialiNonValideException(errori);
        }
        //cerco l'utente con l'id passato come parametro
        Optional<Utente> utente = utenteRepo.findById(idUtente);
        //se l'utente non è presente, lancio un'eccezione
        if(utente.isEmpty()) {
            throw new UtenteNonTrovatoException("Utente non trovato");
        }
        //se il ruolo dell'utente è manager, lancio un'eccezione
        if(utente.get().getRuolo().equals(Ruolo.MANAGER)){
            throw new RuoloNonValidoException("Non è possibile eliminare un manager");
        }
        //se i controlli sono stati superati, elimino l'utente dal database
        utenteRepo.delete(utente.get());
    }

    //metodo per il manager che trova tutti i clienti
    @Override
    public List<UtenteManagerResponseDTO> trovaClienti() {
        //trovo tutti gli utenti
        List<Utente> utenti = utenteRepo.findAll();
        //creo una lista di utenti da restituire tramite il DTO
        List<UtenteManagerResponseDTO> utentiResponse = new ArrayList<>();
        //per ogni utente trovato, se il ruolo è cliente, creo un DTO
        for (Utente utente : utenti) {
            if(utente.getRuolo().equals(Ruolo.CLIENTE)) {
                UtenteManagerResponseDTO utenteResponse = new UtenteManagerResponseDTO();
                //setto i campi del DTO con i valori dell'utente
                utenteResponse.setId(utente.getId());
                utenteResponse.setEmail(utente.getEmail());
                utenteResponse.setNome(utente.getNome());
                utenteResponse.setCognome(utente.getCognome());
                utenteResponse.setDataNascita(utente.getDataNascita());
                utenteResponse.setUsername(utente.getUsername());
                utenteResponse.setRuolo(utente.getRuolo());
                //aggiungo il DTO alla lista da restituire
                utentiResponse.add(utenteResponse);
            }
        }
        //restituisco la lista di utenti
        return utentiResponse;
    }

    //metodo per il manager che trova tutti i dipendenti
    @Override
    public List<UtenteManagerResponseDTO> trovaDipendenti() {
        //trovo tutti gli utenti
        List<Utente> utenti = utenteRepo.findAll();
        //creo una lista di utenti da restituire tramite il DTO
        List<UtenteManagerResponseDTO> utentiResponse = new ArrayList<>();
        //per ogni utente trovato, se il ruolo è dipendente, creo un DTO
        for (Utente utente : utenti) {
            if(utente.getRuolo().equals(Ruolo.DIPENDENTE)) {
                UtenteManagerResponseDTO utenteResponse = new UtenteManagerResponseDTO();
                //setto i campi del DTO con i valori dell'utente
                utenteResponse.setId(utente.getId());
                utenteResponse.setEmail(utente.getEmail());
                utenteResponse.setNome(utente.getNome());
                utenteResponse.setCognome(utente.getCognome());
                utenteResponse.setDataNascita(utente.getDataNascita());
                utenteResponse.setUsername(utente.getUsername());
                utenteResponse.setRuolo(utente.getRuolo());
                //aggiungo il DTO alla lista da restituire
                utentiResponse.add(utenteResponse);
            }
        }
        //restituisco la lista di utenti
        return utentiResponse;
    }

    //metodo interno alla classe per controllare i campi della request di registrazione
    private void ControllaSignupRequest(SignupRequestDTO request){
        //dichiaro una mappa per gli errori
        Map<String,String> errori = new TreeMap<>();
        //controllo i campi della request
        //se la request è null, aggiungo un errore alla mappa e lancio un'eccezione
        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }
        //cerco un utente dall'username passato come parametro
        Optional<Utente> utenteCheck = utenteRepo.findByUsername(request.getUsername());
        //se l'utente è presente, aggiungo un errore alla mappa
        if(utenteCheck.isPresent()){
            errori.put("username", "Username già in uso");
        }
        //se l'email è null, vuota o non contiene '@' e '.', aggiungo un errore alla mappa
        if(request.getEmail()==null || request.getEmail().isEmpty() || !request.getEmail().contains("@") || !request.getEmail().contains(".")){
            errori.put("email", "Devi inserire una email valida");
        }
        //se il nome è null o vuoto, aggiungo un errore alla mappa
        if(request.getNome()==null || request.getNome().isEmpty()){
            errori.put("nome", "Devi inserire un nome valido");
        }
        //se il cognome è null o vuoto, aggiungo un errore alla mappa
        if(request.getCognome()==null || request.getCognome().isEmpty()){
            errori.put("cognome", "Devi inserire un cognome valido");
        }
        //se la data di nascita è null, è successiva alla data attuale o precedente al 1900, aggiungo un errore alla mappa
        if(request.getDataNascita()==null || request.getDataNascita().isAfter(LocalDate.now()) || request.getDataNascita().isBefore(LocalDate.of(1900,1,1))){
            errori.put("dataNascita", "Devi inserire una data di nascita valida");
        }
        //se lo username è null o vuoto, aggiungo un errore alla mappa
        if(request.getUsername()==null || request.getUsername().isEmpty()){
            errori.put("username", "Devi inserire uno username valido");
        }
        //se la password è null o vuota, aggiungo un errore alla mappa
        if(request.getPassword()==null || request.getPassword().isEmpty()){
            errori.put("password", "Devi inserire una password valida");
        } else {
            //se la password ripetuta è null o vuota, aggiungo un errore alla mappa
            if (request.getPasswordRipetuta() == null || request.getPasswordRipetuta().isEmpty()) {
                errori.put("passwordRipetuta", "Devi ripetere la password");
            }
            //se la password e la password ripetuta non coincidono, aggiungo un errore alla mappa
            if (!request.getPassword().equals(request.getPasswordRipetuta())) {
                errori.put("password", "Le password non coincidono");
            }
        }

        //se la mappa degli errori non è vuota, lancio un'eccezione
        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }
    }
}
