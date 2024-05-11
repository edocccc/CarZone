package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.ModificaAppuntamentoRequestDTO;
import com.polimi.carzone.dto.request.PrenotazioneManagerRequestDTO;
import com.polimi.carzone.dto.request.PrenotazioneRequestDTO;
import com.polimi.carzone.dto.request.PresaInCaricoRequestDTO;
import com.polimi.carzone.dto.response.*;

import java.util.List;

public interface AppuntamentoService {

    void prenota(PrenotazioneRequestDTO request);

    List<AppuntamentoResponseDTO> trovaAppuntamentiDipendente(long id);

    ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(long idDipendente);

    List<AppuntamentoResponseDTO> trovaAppuntamentiLiberi();

    void prendiInCarico(PresaInCaricoRequestDTO request);

    long trovaIdVeicolo(long idAppuntamento);

    long trovaIdCliente(long idAppuntamento);

    List<RecensioneResponseDTO> trovaRecensioniDipendente(long idDipendente);

    List<DipendenteConRecensioneDTO> trovaDipendentiConRecensioni();

    void registraVendita(long idAppuntamento, boolean venditaConclusa);

    List<AppuntamentoManagerResponseDTO> trovaAppuntamentiPerManager();

    void prenotaPerManager(PrenotazioneManagerRequestDTO request);

    void eliminaAppuntamento(long idAppuntamento);

    void modificaAppuntamento(long idAppuntamento, ModificaAppuntamentoRequestDTO request);
}
