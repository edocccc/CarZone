package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.PrenotazioneRequestDTO;

public interface AppuntamentoService {
    void prenota(PrenotazioneRequestDTO request);
}
