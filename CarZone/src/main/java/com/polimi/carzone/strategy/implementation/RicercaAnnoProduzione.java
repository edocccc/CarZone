package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.strategy.RicercaStrategy;

import java.util.List;

public class RicercaAnnoProduzione implements RicercaStrategy {

    @Override
    public List<Veicolo> ricerca(RicercaRequestDTO request) {
        return List.of();
    }
}
