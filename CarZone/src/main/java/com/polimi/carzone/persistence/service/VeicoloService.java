package com.polimi.carzone.persistence.service;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.response.VeicoloResponseDTO;
import com.polimi.carzone.model.Veicolo;

import java.util.List;

public interface VeicoloService {
    boolean aggiungiVeicolo(AggiuntaVeicoloRequestDTO request);

    List<VeicoloResponseDTO> findAll();
}
