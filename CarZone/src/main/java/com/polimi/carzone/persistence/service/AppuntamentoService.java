package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.*;
import com.polimi.carzone.dto.response.*;

import java.util.List;

public interface AppuntamentoService {

    void prenota(PrenotazioneRequestDTO request,String token);

    List<AppuntamentoResponseDTO> trovaAppuntamentiDipendente(Long id, String token);

    List<AppuntamentoConRecensioneResponseDTO> trovaAppuntamentiCliente(Long id, String token);

    ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(Long idDipendente, String token);

    ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(Long idDipendente);

    List<AppuntamentoResponseDTO> trovaAppuntamentiLiberi();

    void prendiInCarico(PresaInCaricoRequestDTO request);

    long trovaIdVeicolo(Long idAppuntamento, String token);

    long trovaIdCliente(Long idAppuntamento, String token);

    List<RecensioneResponseDTO> trovaRecensioniDipendente(Long idDipendente, String token);

    List<RecensioneResponseDTO> trovaRecensioniDipendente(Long idDipendente);

    List<DipendenteConRecensioneDTO> trovaDipendentiConRecensioni();

    void registraVendita(Long idAppuntamento, boolean venditaConclusa, String token);

    List<AppuntamentoManagerResponseDTO> trovaAppuntamentiPerManager();

    void prenotaPerManager(PrenotazioneManagerRequestDTO request);

    void eliminaAppuntamento(Long idAppuntamento);

    void modificaAppuntamento(Long idAppuntamento, ModificaAppuntamentoRequestDTO request);

    void lasciaRecensione(LasciaRecensioneRequestDTO request, String token);

    List<RecensioneClienteResponseDTO> trovaRecensioniCliente(Long idCliente, String token);

    AppuntamentoModificaResponseDTO trovaPerModifica(Long idAppuntamento, String token);
}
