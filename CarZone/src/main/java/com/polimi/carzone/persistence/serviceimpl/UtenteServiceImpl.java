package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.LoginRequestDTO;
import com.polimi.carzone.dto.request.SignupRequestDTO;
import com.polimi.carzone.exception.CredenzialiNonValideException;
import com.polimi.carzone.exception.UtenteNonTrovatoException;
import com.polimi.carzone.model.Ruolo;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.persistence.repository.UtenteRepository;
import com.polimi.carzone.persistence.service.UtenteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Service
@Transactional
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepo;

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
        Optional<Utente> utente = utenteRepo.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if(utente.isPresent()) {
            return utente.get();
        } else {
            throw new UtenteNonTrovatoException("Non ci sono utenti con queste credenziali");
        }
    }

    @Override
    public boolean registrazioneCliente(SignupRequestDTO request) {
        boolean controlloRequest = ControllaSignupRequest(request);
        if(!controlloRequest){
            return false;
        }

        Utente utente = new Utente();
        utente.setEmail(request.getEmail());
        utente.setNome(request.getNome());
        utente.setCognome(request.getCognome());
        utente.setDataNascita(request.getDataNascita());
        utente.setUsername(request.getUsername());
        utente.setPassword(request.getPassword());
        utente.setRuolo(Ruolo.CLIENTE);
        utenteRepo.save(utente);
        return true;
    }

    @Override
    public boolean registrazioneDipendente(SignupRequestDTO request) {
        boolean controlloRequest = ControllaSignupRequest(request);
        if(!controlloRequest){
            return false;
        }

        Utente utente = new Utente();
        utente.setEmail(request.getEmail());
        utente.setNome(request.getNome());
        utente.setCognome(request.getCognome());
        utente.setDataNascita(request.getDataNascita());
        utente.setUsername(request.getUsername());
        utente.setPassword(request.getPassword());
        utente.setRuolo(Ruolo.DIPENDENTE);
        utenteRepo.save(utente);
        return true;
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

    private boolean ControllaSignupRequest(SignupRequestDTO request){
        Map<String,String> errori = new TreeMap<>();
        if(request == null){
            errori.put("request", "La request non può essere null");
            throw new CredenzialiNonValideException(errori);
        }
        Optional<Utente> utenteCheck = utenteRepo.findByUsername(request.getUsername());
        if(utenteCheck.isPresent()){
            errori.put("username", "Username già in uso");
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

        return true;
    }
}
