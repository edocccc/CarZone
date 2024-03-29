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
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepo;

    @Override
    public Utente findByUsername(String username) {
        // sostituire null con .orElseThrow(UtenteNonTrovatoException::new)
        return utenteRepo.findByUsername(username).orElse(null);
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
            return null;
        }
    }

    @Override
    public boolean registrazioneCliente(SignupRequestDTO request) {
        //TODO implementare i controlli sulla request
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
        //implementare i controlli sulla request
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
}
