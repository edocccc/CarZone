package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.PrenotazioneRequestDTO;
import com.polimi.carzone.dto.request.PresaInCaricoRequestDTO;
import com.polimi.carzone.dto.response.AppuntamentoResponseDTO;
import com.polimi.carzone.dto.response.PresaInCaricoResponseDTO;
import com.polimi.carzone.dto.response.ValutazioneMediaResponseDTO;

import java.util.List;

public interface AppuntamentoService {

    void prenota(PrenotazioneRequestDTO request);

    List<AppuntamentoResponseDTO> trovaAppuntamentiDipendente(long id);

    ValutazioneMediaResponseDTO calcolaValutazioneMediaDipendente(long idDipendente);

    List<AppuntamentoResponseDTO> trovaAppuntamentiLiberi();

    void prendiInCarico(PresaInCaricoRequestDTO request);
}
