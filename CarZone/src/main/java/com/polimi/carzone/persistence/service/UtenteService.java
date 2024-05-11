package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.LoginRequestDTO;
import com.polimi.carzone.dto.request.ModificaUtenteRequestDTO;
import com.polimi.carzone.dto.request.SignupRequestDTO;
import com.polimi.carzone.dto.response.UtenteManagerResponseDTO;
import com.polimi.carzone.model.Utente;

import java.util.List;

public interface UtenteService {

    Utente findByUsername(String username);

    Utente login(LoginRequestDTO request);

    void registrazioneCliente(SignupRequestDTO request);

    void registrazioneDipendente(SignupRequestDTO request);

    Utente findById(long id);

    List<UtenteManagerResponseDTO> trovaUtentiManager();

    UtenteManagerResponseDTO trovaUtenteManager(long id);

    void modificaUtente(long idUtente, ModificaUtenteRequestDTO request);

    void eliminaUtente(long idUtente);

    List<UtenteManagerResponseDTO> trovaClienti();

    List<UtenteManagerResponseDTO> trovaDipendenti();
}
