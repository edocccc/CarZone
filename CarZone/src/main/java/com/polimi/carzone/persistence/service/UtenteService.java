package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.LoginRequestDTO;
import com.polimi.carzone.dto.request.ModificaUtenteRequestDTO;
import com.polimi.carzone.dto.request.SignupRequestDTO;
import com.polimi.carzone.dto.response.UtenteManagerResponseDTO;
import com.polimi.carzone.model.Utente;

import java.util.List;

public interface UtenteService {
    //la classe service è un'interfaccia che definisce i metodi che, una volta implementati, verranno chiamati dal controller
    //i metodi dichiarati insieme alla loro implementazione comporranno la logica di business dell'applicazione riguardante la gestione degli utenti

    //metodo che a partire da un username, trova un utente
    Utente findByUsername(String username);

    //metodo che a partire da una richiesta, permette il login di un utente
    Utente login(LoginRequestDTO request);

    //metodo che a partire da una richiesta, permette la registrazione di un cliente
    void registrazioneCliente(SignupRequestDTO request);

    //metodo che a partire da una richiesta, permette la registrazione di un dipendente ad un manager
    void registrazioneDipendente(SignupRequestDTO request);

    //metodo che a partire da un id, trova un utente
    Utente findById(long id);

    //metodo per il manager che trova tutti gli utenti
    List<UtenteManagerResponseDTO> trovaUtentiManager();

    //metodo per il manager che a partire da un id, trova un utente
    UtenteManagerResponseDTO trovaUtenteManager(Long id);

    //metodo per il manager che a partire da una richiesta, modifica un utente
    void modificaUtente(Long idUtente, ModificaUtenteRequestDTO request);

    //metodo per il manager che a partire da un id, elimina un utente
    void eliminaUtente(Long idUtente);

    //metodo per il manager che trova tutti i clienti
    List<UtenteManagerResponseDTO> trovaClienti();

    //metodo per il manager che trova tutti i dipendenti
    List<UtenteManagerResponseDTO> trovaDipendenti();
}
