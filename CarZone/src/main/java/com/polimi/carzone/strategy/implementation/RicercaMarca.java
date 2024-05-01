package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RicercaMarca implements RicercaStrategy {

    private final VeicoloService veicoloService;

    @Override
    public List<Veicolo> ricerca(RicercaRequestDTO request) {
        List<Veicolo> veicoli;
        String marcaCercata = request.getMarca();
        veicoli = veicoloService.findByMarca(marcaCercata);
        return veicoli;
    }
}
