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

@Service
@Transactional
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Utente findByUsername(String username) {
        Optional<Utente> utente = utenteRepo.findByUsername(username);
        if(utente.isPresent()) {
            return utente.get();
        } else {
            throw new UtenteNonTrovatoException("Utente non trovato");
        }
    }

    @Override
    public Utente login(LoginRequestDTO request) {
        Map<String,String> errori = new TreeMap<>();

        if(request == null) {
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }
        if(request.getUsername() == null || request.getUsername().isEmpty() || request.getUsername().isBlank()) {
            errori.put("username", "Devi inserire uno username valido");
        }
        if(request.getPassword() == null || request.getPassword().isEmpty() || request.getPassword().isBlank()){
            errori.put("password", "Devi inserire una password valida");
        }
        if(!errori.isEmpty()) {
            throw new CredenzialiNonValideException(errori);
        }
        Optional<Utente> utente = utenteRepo.findByUsername(request.getUsername());
        if(utente.isEmpty()) {
            throw new UtenteNonTrovatoException("Non ci sono utenti con queste credenziali");
        }
        if(!passwordEncoder.matches(request.getPassword(), utente.get().getPassword())) {
            throw new UtenteNonTrovatoException("Non ci sono utenti con queste credenziali");
        }
        return utente.get();
    }

    @Override
    public void registrazioneCliente(SignupRequestDTO request) {
        ControllaSignupRequest(request);

        Utente utente = new Utente();
        utente.setEmail(request.getEmail());
        utente.setNome(request.getNome());
        utente.setCognome(request.getCognome());
        utente.setDataNascita(request.getDataNascita());
        utente.setUsername(request.getUsername());
        utente.setPassword(passwordEncoder.encode(request.getPassword()));
        utente.setRuolo(Ruolo.CLIENTE);
        utenteRepo.save(utente);
    }

    @Override
    public void registrazioneDipendente(SignupRequestDTO request) {
        ControllaSignupRequest(request);

        Utente utente = new Utente();
        utente.setEmail(request.getEmail());
        utente.setNome(request.getNome());
        utente.setCognome(request.getCognome());
        utente.setDataNascita(request.getDataNascita());
        utente.setUsername(request.getUsername());
        utente.setPassword(request.getPassword());
        utente.setRuolo(Ruolo.DIPENDENTE);
        utenteRepo.save(utente);
    }

    @Override
    public Utente findById(long id) {
        Map<String,String> errori = new TreeMap<>();
        if(id <= 0){
            errori.put("id", "L'id dell'utente non è valido");
            throw new CredenzialiNonValideException(errori);
        }
        Optional<Utente> utente = utenteRepo.findById(id);
        if(utente.isPresent()) {
            return utente.get();
        } else {
            throw new UtenteNonTrovatoException("Utente non trovato");
        }
    }

    @Override
    public List<UtenteManagerResponseDTO> trovaUtentiManager() {
        List<Utente> utenti = utenteRepo.findAll();
        List<UtenteManagerResponseDTO> utentiResponse = new ArrayList<>();
        for (Utente utente : utenti) {
            UtenteManagerResponseDTO utenteResponse = new UtenteManagerResponseDTO();
            utenteResponse.setId(utente.getId());
            utenteResponse.setEmail(utente.getEmail());
            utenteResponse.setNome(utente.getNome());
            utenteResponse.setCognome(utente.getCognome());
            utenteResponse.setDataNascita(utente.getDataNascita());
            utenteResponse.setUsername(utente.getUsername());
            utenteResponse.setRuolo(utente.getRuolo());
            utentiResponse.add(utenteResponse);
        }
        return utentiResponse;
    }

    @Override
    public UtenteManagerResponseDTO trovaUtenteManager(Long id) {
        Map<String,String> errori = new TreeMap<>();
        if(id == null || id <= 0){
            errori.put("id", "L'id non è valido");
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Utente> utente = utenteRepo.findById(id);
        if(utente.isEmpty()){
            throw new UtenteNonTrovatoException("Utente non trovato");
        }
        UtenteManagerResponseDTO utenteResponse = new UtenteManagerResponseDTO();
        utenteResponse.setId(utente.get().getId());
        utenteResponse.setEmail(utente.get().getEmail());
        utenteResponse.setNome(utente.get().getNome());
        utenteResponse.setCognome(utente.get().getCognome());
        utenteResponse.setDataNascita(utente.get().getDataNascita());
        utenteResponse.setUsername(utente.get().getUsername());
        utenteResponse.setRuolo(utente.get().getRuolo());
        return utenteResponse;
    }

    @Override
    public void modificaUtente(Long idUtente, ModificaUtenteRequestDTO request) {
        Map<String,String> errori = new TreeMap<>();
        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }
        if(idUtente == null || idUtente <= 0){
            errori.put("idUtente", "L'id dell'utente non è valido");
        }
        if(request.getEmail()==null || request.getEmail().isEmpty()){
            errori.put("email", "Devi inserire una email valida");
        }
        if(request.getNome()==null || request.getNome().isEmpty()){
            errori.put("nome", "Devi inserire un nome valido");
        }
        if(request.getCognome()==null || request.getCognome().isEmpty()){
            errori.put("cognome", "Devi inserire un cognome valido");
        }
        if(request.getDataNascita()==null || request.getDataNascita().isAfter(LocalDate.now()) || request.getDataNascita().isBefore(LocalDate.of(1900,1,1))){
            errori.put("dataNascita", "Devi inserire una data di nascita valida");
        }
        if(request.getUsername()==null || request.getUsername().isEmpty()){
            errori.put("username", "Devi inserire uno username valido");
        }
        Ruolo ruolo = null;

        if(request.getRuolo() == null || request.getRuolo().isEmpty() || request.getRuolo().isBlank()) {
            errori.put("ruolo", "Devi inserire un ruolo valido");
        } else {
            ruolo = switch (request.getRuolo()) {
                case "CLIENTE" -> Ruolo.CLIENTE;
                case "DIPENDENTE" -> Ruolo.DIPENDENTE;
                case "MANAGER" -> Ruolo.MANAGER;
                default -> throw new RuoloNonValidoException("Tipo di ruolo non valido");
            };
        }
        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }

        Optional<Utente> utente = utenteRepo.findById(idUtente);
        if(utente.isEmpty()){
            throw new UtenteNonTrovatoException("Utente non trovato");
        }
        if(utente.get().getRuolo().equals(Ruolo.MANAGER)){
            throw new RuoloNonValidoException("Non è possibile modificare un manager");
        }

        utente.get().setEmail(request.getEmail());
        utente.get().setNome(request.getNome());
        utente.get().setCognome(request.getCognome());
        utente.get().setDataNascita(request.getDataNascita());
        utente.get().setUsername(request.getUsername());
        utente.get().setRuolo(ruolo);
        utenteRepo.save(utente.get());
    }

    @Override
    public void eliminaUtente(Long idUtente) {
        Map<String,String> errori = new TreeMap<>();
        if(idUtente == null || idUtente <= 0) {
            errori.put("id", "Id utente non valido");
            throw new CredenzialiNonValideException(errori);
        }
        Optional<Utente> utente = utenteRepo.findById(idUtente);
        if(utente.isEmpty()) {
            throw new UtenteNonTrovatoException("Utente non trovato");
        }
        if(utente.get().getRuolo().equals(Ruolo.MANAGER)){
            throw new RuoloNonValidoException("Non è possibile eliminare un manager");
        }
        utenteRepo.delete(utente.get());
    }

    @Override
    public List<UtenteManagerResponseDTO> trovaClienti() {
        List<Utente> utenti = utenteRepo.findAll();
        List<UtenteManagerResponseDTO> utentiResponse = new ArrayList<>();
        for (Utente utente : utenti) {
            if(utente.getRuolo().equals(Ruolo.CLIENTE)) {
                UtenteManagerResponseDTO utenteResponse = new UtenteManagerResponseDTO();
                utenteResponse.setId(utente.getId());
                utenteResponse.setEmail(utente.getEmail());
                utenteResponse.setNome(utente.getNome());
                utenteResponse.setCognome(utente.getCognome());
                utenteResponse.setDataNascita(utente.getDataNascita());
                utenteResponse.setUsername(utente.getUsername());
                utenteResponse.setRuolo(utente.getRuolo());
                utentiResponse.add(utenteResponse);
            }
        }
        return utentiResponse;
    }

    @Override
    public List<UtenteManagerResponseDTO> trovaDipendenti() {
        List<Utente> utenti = utenteRepo.findAll();
        List<UtenteManagerResponseDTO> utentiResponse = new ArrayList<>();
        for (Utente utente : utenti) {
            if(utente.getRuolo().equals(Ruolo.DIPENDENTE)) {
                UtenteManagerResponseDTO utenteResponse = new UtenteManagerResponseDTO();
                utenteResponse.setId(utente.getId());
                utenteResponse.setEmail(utente.getEmail());
                utenteResponse.setNome(utente.getNome());
                utenteResponse.setCognome(utente.getCognome());
                utenteResponse.setDataNascita(utente.getDataNascita());
                utenteResponse.setUsername(utente.getUsername());
                utenteResponse.setRuolo(utente.getRuolo());
                utentiResponse.add(utenteResponse);
            }
        }
        return utentiResponse;
    }

    private void ControllaSignupRequest(SignupRequestDTO request){
        Map<String,String> errori = new TreeMap<>();
        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }
        Optional<Utente> utenteCheck = utenteRepo.findByUsername(request.getUsername());
        if(utenteCheck.isPresent()){
            errori.put("username", "Username già in uso");
        }
        if(request.getEmail()==null || request.getEmail().isEmpty() || !request.getEmail().contains("@") || !request.getEmail().contains(".")){
            errori.put("email", "Devi inserire una email valida");
        }
        if(request.getNome()==null || request.getNome().isEmpty()){
            errori.put("nome", "Devi inserire un nome valido");
        }
        if(request.getCognome()==null || request.getCognome().isEmpty()){
            errori.put("cognome", "Devi inserire un cognome valido");
        }
        if(request.getDataNascita()==null || request.getDataNascita().isAfter(LocalDate.now()) || request.getDataNascita().isBefore(LocalDate.of(1900,1,1))){
            errori.put("dataNascita", "Devi inserire una data di nascita valida");
        }
        if(request.getUsername()==null || request.getUsername().isEmpty()){
            errori.put("username", "Devi inserire uno username valido");
        }
        if(request.getPassword()==null || request.getPassword().isEmpty()){
            errori.put("password", "Devi inserire una password valida");
        } else {
            if (request.getPasswordRipetuta() == null || request.getPasswordRipetuta().isEmpty()) {
                errori.put("passwordRipetuta", "Devi ripetere la password");
            }
            if (!request.getPassword().equals(request.getPasswordRipetuta())) {
                errori.put("password", "Le password non coincidono");
            }
        }

        if(!errori.isEmpty()){
            throw new CredenzialiNonValideException(errori);
        }
    }
}
