package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.LoginRequestDTO;
import com.polimi.carzone.dto.request.SignupRequestDTO;
import com.polimi.carzone.model.Ruolo;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.persistence.repository.UtenteRepository;
import com.polimi.carzone.persistence.service.UtenteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utente non trovato");
        }
    }

    @Override
    public Utente login(LoginRequestDTO request) {
        if(request == null ||
                request.getUsername().isEmpty() ||
                request.getUsername().isBlank() ||
                request.getPassword().isEmpty() ||
                request.getPassword().isBlank()) {
            return null;
        }
        Optional<Utente> utente = utenteRepo.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if(utente.isPresent()) {
            return utente.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenziali non valide");
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
        utente.setDataNascita(request.getDataNascita());
        utente.setUsername(request.getUsername());
        utente.setPassword(request.getPassword());
        utente.setRuolo(Ruolo.CLIENTE);
        try {
            utenteRepo.save(utente);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        utente.setDataNascita(request.getDataNascita());
        utente.setUsername(request.getUsername());
        utente.setPassword(request.getPassword());
        utente.setRuolo(Ruolo.DIPENDENTE);
        try {
            utenteRepo.save(utente);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean ControllaSignupRequest(SignupRequestDTO request){
        if(request == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La richiesta non può essere vuota");
        }
        Optional<Utente> utenteCheck = utenteRepo.findByUsername(request.getUsername());
        if(utenteCheck.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username già in uso");
        }
        if(request.getEmail()==null || request.getEmail().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Devi inserire una email valida");
        }
        if(request.getDataNascita()==null || request.getDataNascita().isAfter(LocalDate.now()) || request.getDataNascita().isBefore(LocalDate.of(1900,1,1))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Devi inserire una data di nascita valida");
        }
        if(request.getUsername()==null || request.getUsername().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Devi inserire uno username valido");
        }
        if(request.getPassword()==null || request.getPassword().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Devi inserire una password valida");
        }

        return true;
    }
}
