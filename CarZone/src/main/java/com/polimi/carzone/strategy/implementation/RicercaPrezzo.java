package com.polimi.carzone.strategy.implementation;

import com.polimi.carzone.dto.request.RicercaRequestDTO;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.service.VeicoloService;
import com.polimi.carzone.strategy.RicercaStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RicercaPrezzo implements RicercaStrategy {

    private final VeicoloService veicoloService;

    @Override
    public List<Veicolo> ricerca(RicercaRequestDTO request) {
        List<Veicolo> veicoli;
        Double prezzoMinimoCercato = request.getPrezzoMinimo();
        Double prezzoMassimoCercato = request.getPrezzoMassimo();
        veicoli = veicoloService.findByPrezzo(prezzoMinimoCercato, prezzoMassimoCercato);
        return veicoli;
    }
}
