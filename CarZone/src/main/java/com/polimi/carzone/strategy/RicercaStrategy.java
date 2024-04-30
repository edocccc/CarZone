package com.polimi.carzone.strategy;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;

import java.util.List;

public interface RicercaStrategy {
    List<Veicolo> ricerca(RicercaRequestDTO request);
}
