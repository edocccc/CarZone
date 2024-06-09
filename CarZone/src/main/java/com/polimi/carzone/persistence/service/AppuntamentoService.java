package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.*;
import com.polimi.carzone.dto.response.*;

import java.io.IOException;
import java.util.List;

public interface AppuntamentoService {
    //la classe service è un'interfaccia che definisce i metodi che, una volta implementati, verranno chiamati dal controller
    //i metodi dichiarati insieme alla loro implementazione comporranno la logica di business dell'applicazione riguardante la gestione degli appuntamenti

    //metodo che a partire da una richiesta e un token, prenota un appuntamento
    void prenota(PrenotazioneRequestDTO request,String token) throws IOException;

    //metodo che a partire da un id e un token, trova gli appuntamenti del dipendente corrispondente all'id
    List<AppuntamentoResponseDTO> trovaAppuntamentiDipendente(Long id, String token);

    //metodo che a partire da un id e un token, trova gli appuntamenti del cliente corrispondente all'id
    List<AppuntamentoConRecensioneResponseDTO> trovaAppuntamentiCliente(Long id, String token);

    //metodo che a partire da un id e un token, calcola la valutazione media del dipendente corrispondente all'id
    ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(Long idDipendente, String token);

    //metodo che a partire da un id, calcola la valutazione media del dipendente corrispondente all'id
    //l'overload del metodo serve a permettere la chiamata del metodo senza token da parte del manager
    ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(Long idDipendente);

    //metodo che trova gli appuntamenti liberi (non ancora presi in carico da un dipendente)
    List<AppuntamentoResponseDTO> trovaAppuntamentiLiberi();

    //metodo che a partire da una richiesta e un token, permette al dipendente di prendere in carico un appuntamento
    void prendiInCarico(PresaInCaricoRequestDTO request, String token);

    //metodo che a partire da un id di un appuntamento e un token, trova l'id del veicolo associato all'appuntamento
    long trovaIdVeicolo(Long idAppuntamento, String token);

    //metodo che a partire da un id di un appuntamento e un token, trova l'id del cliente associato all'appuntamento
    long trovaIdCliente(Long idAppuntamento, String token);

    //metodo che a partire da un id di un dipendente e un token, trova tutte le recensioni associate al dipendente
    List<RecensioneResponseDTO> trovaRecensioniDipendente(Long idDipendente, String token);

    //metodo che a partire da un id di un dipendente, trova tutte le recensioni associate al dipendente
    //l'overload del metodo serve a permettere la chiamata del metodo senza token da parte del manager
    List<RecensioneResponseDTO> trovaRecensioniDipendente(Long idDipendente);

    //metodo che trova per ogni dipendente la valutazione media e tutte le recensioni associate
    List<DipendenteConRecensioneDTO> trovaDipendentiConRecensioni();

    //metodo che permette di registrare la vendita di un veicolo associato a un appuntamento
    //richiede l'id dell'appuntamento, un booleano che indica se la vendita è stata conclusa e un token
    void registraVendita(Long idAppuntamento, boolean venditaConclusa, String token);

    //metodo per il manager che permette di trovare tutti gli appuntamenti
    List<AppuntamentoManagerResponseDTO> trovaAppuntamentiPerManager();

    //metodo per il manager che permette di prenotare un appuntamento
    void prenotaPerManager(PrenotazioneManagerRequestDTO request);

    //metodo per il manager che permette di eliminare un appuntamento
    void eliminaAppuntamento(Long idAppuntamento);

    //metodo per il manager che permette di modificare un appuntamento
    void modificaAppuntamento(Long idAppuntamento, ModificaAppuntamentoRequestDTO request);

    //metodo che a partire da una richiesta e un token, permette al cliente di lasciare una recensione
    void lasciaRecensione(LasciaRecensioneRequestDTO request, String token);

    //metodo che a partire da un id di un cliente e un token, trova tutte le recensioni associate al cliente
    List<RecensioneClienteResponseDTO> trovaRecensioniCliente(Long idCliente, String token);

    //metodo per il manager che permette di trovare un appuntaemnto per la modifica a partire da un id e un token
    AppuntamentoModificaResponseDTO trovaPerModifica(Long idAppuntamento, String token);
}
