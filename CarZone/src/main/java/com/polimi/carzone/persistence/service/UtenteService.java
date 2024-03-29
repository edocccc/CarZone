package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.LoginRequestDTO;
import com.polimi.carzone.dto.request.SignupRequestDTO;
import com.polimi.carzone.model.Utente;

public interface UtenteService {

    Utente findByUsername(String username);

    Utente login(LoginRequestDTO request);

    boolean registrazioneCliente(SignupRequestDTO request);

    boolean registrazioneDipendente(SignupRequestDTO request);
}
